package com.wanted.socialMediaIntegratedFeed.global.jwt;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

/**
 * JWT 인증 작업 중 오류 발생 시 던져지는 예외(Exception) 클래스입니다.
 * @author 정성국
 */
@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final JwtError error;

    public JwtAuthenticationException(JwtError error, String message) {
        super(message);
        Assert.notNull(error, "error must not be null");
        this.error = error;
    }

}
