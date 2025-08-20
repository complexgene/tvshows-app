package com.avatarconnex.tvshowsbe.service;

import com.avatarconnex.tvshowsbe.configs.AppConfigs;
import com.avatarconnex.tvshowsbe.exceptions.TvShowExternalDataFetchException;
import com.avatarconnex.tvshowsbe.mapper.TVShowMapper;
import com.avatarconnex.tvshowsbe.models.AppResponse;
import com.avatarconnex.tvshowsbe.models.ShowListItemDto;
import com.avatarconnex.tvshowsbe.models.TVShowDetails;
import com.avatarconnex.tvshowsbe.repository.TVShowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TVShowService {

    private final TVShowRepository repository;
    private final ValidationService validationService;
    private final TVShowMapper tvShowMapper;
    private final RestTemplate restTemplate;
    private final TvShowDetailsFetcherFactory factory;
    private final AppConfigs appConfigs;

    @Value("${app.configs.tvtitles-path:tvtitles.txt}")
    private String tvTitlesPath;

    @Autowired
    public TVShowService(TVShowRepository repository, ValidationService validationService,
                         TVShowMapper tvShowMapper,
                         RestTemplate restTemplate,
                         AppConfigs appConfigs,
                         TvShowDetailsFetcherFactory factory) {
        this.repository = repository;
        this.validationService = validationService;
        this.tvShowMapper = tvShowMapper;
        this.restTemplate = restTemplate;
        this.factory = factory;
        this.appConfigs = appConfigs;
    }


    public List<TVShowDetails> fetchAllTvShows() {
        return repository.findAll();
    }

    public Page<ShowListItemDto> fetchAllTVShows(Pageable pageable) {
        return repository.findAll(pageable).map(tvShowMapper::toListDto);
    }

    /**
     * Parallelized import: reads all titles, validates, then performs bounded parallel fetch/save.
     *
     * @return
     */
    public AppResponse importFromTitles(InputStream inputStream) {
        final List<String> titles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String title;
            while ((title = reader.readLine()) != null) {
                if (!validationService.isValidTvShowName(title)) {
                    log.debug("Skipping invalid title: {}", title);
                    continue;
                }
                titles.add(title.trim());
            }
        } catch (IOException ioe) {
            log.error("Issue while reading titles: {}", ioe.getMessage());
            throw new RuntimeException("Import failed while reading titles: " + ioe.getMessage(), ioe);
        }

        if (titles.isEmpty()) {
            log.info("No valid titles found to import.");
            return null;
        }

        ITvShowDetailsFetcher dataFetcher = factory.getShowFetcher(appConfigs.getFetchStrategy());
        return dataFetcher.importTvShowDataFromTitles(titles);

    }

    public AppResponse importShowDataFromTitles() {
        log.debug("Starting import of TV shows from titles at path: {}", tvTitlesPath);
        if (!validationService.isValidTvShowTitlePath(tvTitlesPath)) {
            log.error("Invalid TV titles path: {}", tvTitlesPath);
            throw new TvShowExternalDataFetchException("Import failed: Invalid TV titles path: " + tvTitlesPath);
        }
        ClassPathResource resource = new ClassPathResource(tvTitlesPath);
        try (InputStream inputStream = resource.getInputStream()) {
            return importFromTitles(inputStream);
        } catch (IOException ioe) {
            log.error("Error reading TV titles from resource '{}': {}", tvTitlesPath, ioe.getMessage());
            throw new TvShowExternalDataFetchException("Import failed: " + ioe.getMessage(), ioe);
        } catch (Exception e) {
            log.error("Error importing TV shows from titles: {}", e.getMessage());
            throw new TvShowExternalDataFetchException("Import failed: " + e.getMessage(), e);
        }
    }

    public TVShowDetails fetchShowDetail(Long showId) {
        return repository.findById(showId).get();
    }
}
