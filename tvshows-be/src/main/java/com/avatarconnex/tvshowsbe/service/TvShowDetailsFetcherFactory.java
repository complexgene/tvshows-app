package com.avatarconnex.tvshowsbe.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TvShowDetailsFetcherFactory {
    Map<FetchStrategy, ITvShowDetailsFetcher> iTvShowDetailsFetcherMap = new HashMap<>();

    void register(ITvShowDetailsFetcher iTvShowDetailsFetcher) {
        iTvShowDetailsFetcherMap.put(iTvShowDetailsFetcher.getStrategy(), iTvShowDetailsFetcher);
    }

    ITvShowDetailsFetcher getShowFetcher(FetchStrategy fetchStrategy) {
        return iTvShowDetailsFetcherMap.get(fetchStrategy);
    }
}
