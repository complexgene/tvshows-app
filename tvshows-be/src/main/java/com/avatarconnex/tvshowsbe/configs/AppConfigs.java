package com.avatarconnex.tvshowsbe.configs;

import com.avatarconnex.tvshowsbe.service.FetchStrategy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppConfigs {

    @Value("${app.data.fetch-strategy}")
    FetchStrategy fetchStrategy;

    @Value("${app.configs.tvtitles-path:tvtitles.txt}")
    private String tvTitlesPath;

}
