package com.wanted.socialMediaIntegratedFeed.global.common;

import lombok.Getter;

@Getter
public enum RespnoseStatusValue {
    /**
     * 200 : 요청 성공
     */
    SUCCESS("200", "요청에 성공하였습니다."),

    /**
     * 400 : Request, Response 오류
     */
    NOT_FOUND_CONTENT("CNT404", "게시글을 찾을 수 없습니다.");

    private final String code;
    private final String message;

    RespnoseStatusValue(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
