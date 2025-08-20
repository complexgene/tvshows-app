package com.avatarconnex.tvshowsbe.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class HttpCallService {
    @Autowired private RestTemplate restTemplate;

    @Value("${app.tvmazev2.base-url:http://api.tvmazev2.com}")
    private String tvmazev2BaseUrl;

    public String fetchData(String title) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(tvmazev2BaseUrl + "/singlesearch/shows")
                .queryParam("q", title)
                .build(true)
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);
        if (response == null || response.isBlank()) {
            log.debug("Empty response for '{}'", title);
            return response;
        }
        return response;
    }

}
