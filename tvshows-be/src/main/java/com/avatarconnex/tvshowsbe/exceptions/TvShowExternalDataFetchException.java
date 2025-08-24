package com.avatarconnex.tvshowsbe.exceptions;

public class TvShowExternalDataFetchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TvShowExternalDataFetchException(String message) {
        super(message);
    }

    public TvShowExternalDataFetchException(ErrorCodes errorCode) {
        super(errorCode.getErrorDesc());
    }

    public TvShowExternalDataFetchException(ErrorCodes errorCodes, Throwable throwable) {
        super(errorCodes.getErrorDesc());
    }

    public TvShowExternalDataFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
