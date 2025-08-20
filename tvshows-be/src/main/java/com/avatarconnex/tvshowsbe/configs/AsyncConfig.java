package com.avatarconnex.tvshowsbe.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Bounded pool for parallel TVMaze calls.
     * Tune pool sizes if needed (env/props can override via @Value).
     */
    @Bean(name = "importExecutor")
    public Executor importExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setThreadNamePrefix("tv-import-");
        ex.setCorePoolSize(8);        // safe default on dev laptop
        ex.setMaxPoolSize(16);        // upper bound
        ex.setQueueCapacity(200);     // backpressure queue
        ex.setAllowCoreThreadTimeOut(true);
        ex.initialize();
        return ex;
    }

    /**
     * Shared RestTemplate with sensible timeouts.
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
        rf.setConnectTimeout(5_000);
        rf.setReadTimeout(8_000);
        return new RestTemplate(rf);
    }
}
