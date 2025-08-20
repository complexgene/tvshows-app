package com.avatarconnex.tvshowsbe.service;

import com.avatarconnex.tvshowsbe.exceptions.TvShowExternalDataFetchException;
import com.avatarconnex.tvshowsbe.mapper.TVShowMapper;
import com.avatarconnex.tvshowsbe.models.AppResponse;
import com.avatarconnex.tvshowsbe.models.ShowListItemDto;
import com.avatarconnex.tvshowsbe.models.TVShowDetails;
import com.avatarconnex.tvshowsbe.repository.TVShowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class TVShowService {

    private final TVShowRepository repository;
    private final ValidationService validationService;
    private final TVShowMapper tvShowMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.tvmaze.base-url:http://api.tvmaze.com}")
    private String tvMazeBaseUrl;
    @Value("${app.configs.tvtitles-path:tvtitles.txt}")
    private String tvTitlesPath;

    @Autowired
    public TVShowService(TVShowRepository repository, ValidationService validationService,
                         TVShowMapper tvShowMapper) {
        this.repository = repository;
        this.validationService = validationService;
        this.tvShowMapper = tvShowMapper;
        this.restTemplate = new RestTemplate();
    }


    public List<TVShowDetails> fetchAllTvShows() {
        return repository.findAll();
    }

    public Page<ShowListItemDto> fetchAllTVShows(Pageable pageable) {
        return repository.findAll(pageable).map(tvShowMapper::toListDto);
    }


    public void importFromTitles(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String title;
            while ((title = reader.readLine()) != null) {
                if (!validationService.isValidTvShowName(title)) {
                    log.debug("Skipping invalid title: {}", title);
                    continue;
                }

                try {
                    URI uri = UriComponentsBuilder
                            .fromHttpUrl(tvMazeBaseUrl + "/singlesearch/shows")
                            .queryParam("q", title)
                            .build(true)
                            .toUri();

                    String response = restTemplate.getForObject(uri, String.class);
                    if (response == null || response.isBlank()) continue;

                    TVShowDetails show = objectMapper.readValue(response, TVShowDetails.class);
                    if (show != null && show.getName() != null && !show.getName().isBlank()) {
                        repository.save(show);
                    }
                } catch (HttpClientErrorException.NotFound nf) {
                    log.error("Title '{}' not found on TVMaze, skipping.", title);
                    continue;
                } catch (HttpClientErrorException | IllegalArgumentException httpEx) {
                    log.error("Error fetching details for title '{}': {}", title, httpEx.getMessage());
                    continue;
                }
            }
        } catch (Exception e) {
            log.error("Issue while reading titles: {}", e.getMessage());
            throw new RuntimeException("Import failed while reading titles: " + e.getMessage(), e);
        }
    }

    public AppResponse importTvShowDataFromTitles() {
        log.debug("Starting import of TV shows from titles at path: {}", tvTitlesPath);
        if (!validationService.isValidTvShowTitlePath(tvTitlesPath)) {
            log.error("Invalid TV titles path: {}", tvTitlesPath);
            throw new TvShowExternalDataFetchException("Import failed: Invalid TV titles path: " + tvTitlesPath);
        }
        ClassPathResource resource = new ClassPathResource(tvTitlesPath);
        try (InputStream inputStream = resource.getInputStream()) {
            importFromTitles(inputStream);
        } catch (IOException ioe) {
            log.error("Error reading TV titles from resource '{}': {}", tvTitlesPath, ioe.getMessage());
            throw new TvShowExternalDataFetchException("Import failed: " + ioe.getMessage(), ioe);
        } catch (Exception e) {
            log.error("Error importing TV shows from titles: {}", e.getMessage());
            throw new TvShowExternalDataFetchException("Import failed: " + e.getMessage(), e);
        }
        return AppResponse.builder()
                .message("Data Fetched Successfully")
                .code(200)
                .status("success")
                .data(null)
                .build();
    }
}
