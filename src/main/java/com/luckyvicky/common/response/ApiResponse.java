package com.luckyvicky.common.response;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ApiResponse<T> {

    private boolean status = true;

    private T data;

    private Map<String, Object> metaData = new HashMap<>();

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(T data, Map<String, Object> metaData) {
        this.data = data;
        this.metaData = metaData;
    }
}
