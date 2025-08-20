package com.avatarconnex.tvshowsbe.service;

import com.avatarconnex.tvshowsbe.models.AppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SingleThreadShowDetailsFetcher")
public class SingleThreadShowDetailsFetcher extends ITvShowDetailsFetcher {

    @Autowired
    public SingleThreadShowDetailsFetcher(TvShowDetailsFetcherFactory factory) {
        factory.register(this);
    }

    @Override
    public AppResponse importTvShowDataFromTitles(List<String> titles) {
        long startTime = System.currentTimeMillis();
        titles.forEach(this::fetchDataForTitle);
        return AppResponse.builder()
                .message("Data Fetched Successfully")
                .code(HttpStatus.OK)
                .timeTaken(System.currentTimeMillis() - startTime)
                .status("success")
                .build();
    }

    @Override
    public FetchStrategy getStrategy() {
        return FetchStrategy.SINGLE_THREAD;
    }
}
