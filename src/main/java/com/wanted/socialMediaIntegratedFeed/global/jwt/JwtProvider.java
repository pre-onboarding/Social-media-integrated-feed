package com.wanted.socialMediaIntegratedFeed.global.jwt;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.global.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

/**
 * JWT 생성 및 추출하는 기능을 수행하는 클래스입니다.
 * @author 정성국
 */
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtConfig jwtConfig;

    public String generateAccessToken(Member member) {
        return generateToken(member, JwtConfig.ACCESS_TOKEN_EXPIRATION_MS);
    }

    public String generateRefreshToken(Member member) {
        return generateToken(member, JwtConfig.REFRESH_TOKEN_EXPIRATION_MS);
    }

    private String generateToken(Member member, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .header().type("JWT")
                .and()
                .issuer(jwtConfig.getJwtIssuer())
                .issuedAt(now)
                .expiration(expiryDate)
                .subject(member.getId().toString())
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getJwtKey().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .requireIssuer(jwtConfig.getJwtIssuer())
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getJwtKey().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);
    }

}
