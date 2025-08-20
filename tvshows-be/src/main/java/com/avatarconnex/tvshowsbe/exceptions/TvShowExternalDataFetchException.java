package com.avatarconnex.tvshowsbe.exceptions;

public class TvShowExternalDataFetchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TvShowExternalDataFetchException(String message) {
        super(message);
    }

    public TvShowExternalDataFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
