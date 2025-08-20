package com.avatarconnex.tvshowsbe.service;

import com.avatarconnex.tvshowsbe.models.AppResponse;
import com.avatarconnex.tvshowsbe.models.TVShowDetails;
import com.avatarconnex.tvshowsbe.repository.TVShowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.concurrent.Executor;

@Service
@Slf4j
public abstract class ITvShowDetailsFetcher {
    @Autowired protected Executor importExecutor; // bounded pool for parallelism
    @Autowired protected HttpCallService httpCallService;
    @Autowired protected TVShowRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    abstract AppResponse importTvShowDataFromTitles(List<String> titles);
    abstract FetchStrategy getStrategy();

    protected void fetchDataForTitle(String title) {
        try {
            String response = httpCallService.fetchData(title);
            TVShowDetails show = objectMapper.readValue(response, TVShowDetails.class);
            if (show != null && show.getName() != null && !show.getName().isBlank()) {
                repository.save(show); // each task has its own tx/EntityManager
                log.debug("Saved '{}'", show.getName());
            }
        } catch (HttpClientErrorException.NotFound nf) {
            log.warn("Title '{}' not found on tvmazev2, skipping.", title);
        } catch (HttpClientErrorException | IllegalArgumentException httpEx) {
            // Non-fatal per your current behavior; just log and move on
            log.warn("Error fetching '{}' : {}", title, httpEx.getMessage());
        } catch (Exception e) {
            // Defensive catch to prevent a single title from breaking the whole import
            log.error("Unexpected error for '{}': {}", title, e.getMessage());
        }
    }

}
