package com.wanted.socialMediaIntegratedFeed.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 있습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "중복된 유저네임이 있습니다."),
    NON_EXISTENT_MEMBER(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "올바르지 않는 비밀번호입니다."),
    WRONG_AUTH_CODE(HttpStatus.BAD_REQUEST, "올바르지 않는 인증코드입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
