package com.wanted.socialMediaIntegratedFeed.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseResult<T> {
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseResult(RespnoseStatusValue status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
