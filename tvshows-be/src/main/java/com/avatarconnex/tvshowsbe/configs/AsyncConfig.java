package com.avatarconnex.tvshowsbe.configs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// com.avatarconnex.tvshowsbe.config.AsyncConfig.java
@Configuration
public class AsyncConfig {
    @Bean(destroyMethod = "shutdown")
    public Executor importExecutor(
            @Value("${import.parallelism:8}") int parallelism) {
        return Executors.newFixedThreadPool(
                parallelism,
                r -> {
                    Thread t = new Thread(r);
                    t.setName("tvmaze-import-");
                    t.setDaemon(true);
                    return t;
                });
    }
}
