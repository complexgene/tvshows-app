package com.avatarconnex.tvshowsbe.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int code;           // HTTP status code
    private String status;      // e.g., "error"
    private String message;     // human-readable message
    private String errorType;   // e.g., "ValidationError", "ExternalApiError"
    private String path;        // endpoint where error occurred
    private long timestamp;     // epoch time in ms
}
