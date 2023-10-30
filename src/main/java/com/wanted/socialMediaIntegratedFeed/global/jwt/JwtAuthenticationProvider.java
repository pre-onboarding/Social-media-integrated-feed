package com.wanted.socialMediaIntegratedFeed.global.jwt;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * JWT의 인증작업을 수행하는 클래스입니다.
 * Spring Security의 {@link org.springframework.security.authentication.ProviderManager} 의 인수로 사용됩니다.
 * @author 정성국
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public JwtAuthenticationProvider(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    /**
     * {@link BearerAuthenticationToken}을 받아 JWT로 parsing 및 인증한 후 {@link JwtAuthenticationToken}을 반환합니다.
     * @param authentication {@link BearerAuthenticationToken}
     * @return {@link JwtAuthenticationToken}
     * @author 정성국
     */
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        var bearerToken = (BearerAuthenticationToken) authentication;
        Jws<Claims> jws;
        Member member;
        try {
            jws = jwtProvider.parse(bearerToken.getToken());
            // refreshToken 만료시간 확인
            member = memberRepository.findById(Long.parseLong(jws.getPayload().getSubject()))
                    .orElseThrow(() -> new JwtException("존재하지 않는 회원의 JWT"));
            jwtProvider.parse(member.getRefreshToken());
        } catch (JwtException e) {
            JwtError error = new JwtError("invalid_token", HttpStatus.BAD_REQUEST);
            throw new JwtAuthenticationException(error, "유효하지 않은 JWT");
        }

        var auth = new JwtAuthenticationToken(jws, member.getAuthorities());
        auth.setPrincipal(member);

        return auth;
    }

    /**
     * authenticate 메서드의 인수로 받아 인증을 수행할 Token을 지정하고, 사용가능 여부를 반환합니다.
     * @param authentication 인증을 수행할 Token
     * @author 정성국
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return BearerAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
