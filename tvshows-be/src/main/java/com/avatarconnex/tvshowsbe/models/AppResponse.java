package com.avatarconnex.tvshowsbe.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class AppResponse {
    private String message;
    private String status;
    // Adding approx time taken in an operation.
    private Long timeTaken;
    // HttpStatus code
    private HttpStatus code;

    public AppResponse(String message, String status, Long timeTaken, HttpStatus code) {
        this.message = message;
        this.status = status;
        this.timeTaken = timeTaken;
        this.code = code;
    }
}
