package com.avatarconnex.tvshowsbe.service;

import com.avatarconnex.tvshowsbe.models.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("BatchedShowDetailsFetcher")
@Slf4j
public class BatchedShowDetailsFetcher extends ITvShowDetailsFetcher {

    @Autowired
    public BatchedShowDetailsFetcher(TvShowDetailsFetcherFactory factory) {
        factory.register(this);
    }

    // Implementation of the batched external call
    @Override
    public AppResponse importTvShowDataFromTitles(List<String> titles) {
        long startTime = System.currentTimeMillis();
        final int batchSize = 32; // tune if you change pool sizes
        for (List<String> batch : partition(titles, batchSize)) {
            var futures = batch.stream()
                    .map(t -> java.util.concurrent.CompletableFuture.runAsync(() -> fetchDataForTitle(t), importExecutor))
                    .toArray(java.util.concurrent.CompletableFuture[]::new);
            java.util.concurrent.CompletableFuture.allOf(futures).join();
        }
        log.info("Import complete. Processed {} titles.", titles.size());
        return AppResponse.builder()
                .message("Data Fetched Successfully")
                .code(HttpStatus.OK)
                .status("success")
                .timeTaken(System.currentTimeMillis() - startTime)
                .build();
    }

    private static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> parts = new java.util.ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            parts.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return parts;
    }

    @Override
    public FetchStrategy getStrategy() {
        return FetchStrategy.BATCHED;
    }
}
