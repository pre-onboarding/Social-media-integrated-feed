package com.wanted.socialMediaIntegratedFeed.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * JWT (JSON Web Token) 생성을 위한 설정값을 지정합니다.
 * <p>
 * 이 클래스는 Access Token과 Refresh Token의 만료 시간(Expiration Time)을 지정하고, JWT 생성 시 사용할 정보를 포함합니다.
 * </p>
 * @author 정성국
 */
@Getter
@Component
@ConfigurationProperties("jwt")
public class JwtConfig {

    public static final long ACCESS_TOKEN_EXPIRATION_MS = Duration.ofHours(1).toMillis();
    public static final long REFRESH_TOKEN_EXPIRATION_MS = Duration.ofDays(1).toMillis();

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.key}")
    private String jwtKey;

}

