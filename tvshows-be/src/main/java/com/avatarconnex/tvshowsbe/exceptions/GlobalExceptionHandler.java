package com.avatarconnex.tvshowsbe.exceptions;

import com.avatarconnex.tvshowsbe.models.AppResponse;
import com.avatarconnex.tvshowsbe.models.ErrorResponse;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TvShowExternalDataFetchException.class)
    public ResponseEntity<ErrorResponse> handleTvShowExternalDataFetchException(
            TvShowExternalDataFetchException ex,
            HttpServletRequest request) {
        log.error("TvShowExternalDataFetchException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.BAD_GATEWAY.value())
                        .status("error")
                        .message(ex.getMessage())
                        .errorType("TvShowExternalDataFetchException")
                        .path(request.getRequestURI())
                        .timestamp(System.currentTimeMillis())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status("error")
                        .message(ex.getMessage())
                        .errorType(ex.getClass().getSimpleName())
                        .path(request.getRequestURI())
                        .timestamp(System.currentTimeMillis())
                        .build());
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<AppResponse> handleRateLimit(RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(AppResponse.builder()
                        .status("error")
                        .code(HttpStatus.NOT_ACCEPTABLE)
                        .message("Too many requests. Please try again shortly.")
                        .build());
    }
}
