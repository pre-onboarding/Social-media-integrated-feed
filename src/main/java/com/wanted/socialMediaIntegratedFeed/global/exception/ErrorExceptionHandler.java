package com.wanted.socialMediaIntegratedFeed.global.exception;

import com.wanted.socialMediaIntegratedFeed.global.common.RespnoseStatusValue;
import com.wanted.socialMediaIntegratedFeed.global.common.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseResult<RespnoseStatusValue> NotFoundExceptionHandle(NotFoundException exception) {
        return new ResponseResult<>(exception.getStatus());
    }
}
