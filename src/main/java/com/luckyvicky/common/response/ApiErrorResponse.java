package com.luckyvicky.common.response;

import lombok.Getter;

@Getter
public class ApiErrorResponse {

    private boolean status = false;

    private String errorMessage;

    public ApiErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
