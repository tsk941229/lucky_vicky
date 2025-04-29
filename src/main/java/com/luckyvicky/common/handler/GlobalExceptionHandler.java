package com.luckyvicky.common.handler;

import com.luckyvicky.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ApiErrorResponse runtimeException(RuntimeException e) {
        log.error("Runtime Exception :: ", e.getMessage());
        return new ApiErrorResponse(e.getMessage());
    }

}
