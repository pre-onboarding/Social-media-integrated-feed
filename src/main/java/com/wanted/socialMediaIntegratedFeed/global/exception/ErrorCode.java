package com.wanted.socialMediaIntegratedFeed.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 있습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "중복된 유저네임이 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
