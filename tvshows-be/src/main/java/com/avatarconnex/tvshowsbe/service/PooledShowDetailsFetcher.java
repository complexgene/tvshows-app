package com.avatarconnex.tvshowsbe.service;

import com.avatarconnex.tvshowsbe.models.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("PooledShowDetailsFetcher")
@Slf4j
public class PooledShowDetailsFetcher extends ITvShowDetailsFetcher {

    @Autowired
    public PooledShowDetailsFetcher(TvShowDetailsFetcherFactory factory) {
        factory.register(this);
    }

    @Override
    public AppResponse importTvShowDataFromTitles(List<String> titles) {
        long startTime = System.currentTimeMillis();
        // Kick off bounded parallel requests
        List<CompletableFuture<Void>> tasks = titles.stream()
                .map(t -> CompletableFuture.runAsync(() -> fetchDataForTitle(t), importExecutor))
                .toList();

        // Wait for all to complete (propagate if any fatal error bubbles up)
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

        log.info("Import complete. Processed {} titles.", titles.size());

        return AppResponse.builder()
                .message("Data Fetched Successfully")
                .code(HttpStatus.OK)
                .timeTaken(System.currentTimeMillis() - startTime)
                .status("success")
                .build();
    }

    @Override
    public FetchStrategy getStrategy() {
        return FetchStrategy.POOLED;
    }
}
