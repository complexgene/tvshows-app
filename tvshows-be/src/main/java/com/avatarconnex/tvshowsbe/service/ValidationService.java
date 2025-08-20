package com.avatarconnex.tvshowsbe.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    private boolean isValidTVShowId(Long id) {
        return id != null && id > 0;
    }

    private boolean isNonEmptyTVShowTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }

    private boolean isValidTVShowDetails(String details) {
        return details != null && !details.trim().isEmpty();
    }

    private boolean hasSpecialCharsInTVSHowName(String tvShowTitle) {
        return !tvShowTitle.matches("[a-zA-Z0-9]*");
    }

    public boolean isValidTvShowName(String tvShowTitle) {
        return isNonEmptyTVShowTitle(tvShowTitle) && !hasSpecialCharsInTVSHowName(tvShowTitle);
    }

    public boolean isValidTvShowTitlePath(String tvShowTitlePath) {
        return tvShowTitlePath != null && !tvShowTitlePath.trim().isEmpty() && !tvShowTitlePath.isBlank();
    }
}
