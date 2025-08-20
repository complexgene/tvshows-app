package com.avatarconnex.tvshowsbe.exceptions;

import lombok.Getter;

public enum ErrorCodes {
    ER101("Failed to fetch data from remote");

    @Getter private String errorDesc;

    ErrorCodes(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
