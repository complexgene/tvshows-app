package com.avatarconnex.tvshowsbe.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppResponse {
    private String message;
    private Object data;
    private String status;
    private int code;

    public AppResponse(String message, Object data, String status, int code) {
        this.message = message;
        this.data = data;
        this.status = status;
        this.code = code;
    }
}
