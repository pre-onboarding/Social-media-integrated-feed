package com.wanted.socialMediaIntegratedFeed.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SERVER_INTERNAL_ERROR(HttpStatus.BAD_REQUEST, "서버 내부적인 에러");
//    DUPLICATE_EMAIL(409, "중복된 이메일이 있습니다."),
//    DUPLICATE_USERNAME(409, "중복된 유저네임이 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
