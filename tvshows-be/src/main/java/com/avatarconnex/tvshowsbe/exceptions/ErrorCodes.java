package com.avatarconnex.tvshowsbe.exceptions;

import lombok.Getter;

public enum ErrorCodes {
    ER101("Failed to fetch data from remote"),
    ER404("TV Show not found");

    @Getter private String errorDesc;

    ErrorCodes(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
