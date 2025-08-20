package com.avatarconnex.tvshowsbe.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TvShowDetailsFetcherFactory {
    Map<FetchStrategy, ATvShowDetailsFetcher> iTvShowDetailsFetcherMap = new HashMap<>();

    void register(ATvShowDetailsFetcher iTvShowDetailsFetcher) {
        iTvShowDetailsFetcherMap.put(iTvShowDetailsFetcher.getStrategy(), iTvShowDetailsFetcher);
    }

    ATvShowDetailsFetcher getShowFetcher(FetchStrategy fetchStrategy) {
        return iTvShowDetailsFetcherMap.get(fetchStrategy);
    }
}
