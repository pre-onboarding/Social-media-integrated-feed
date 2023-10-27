package com.wanted.socialMediaIntegratedFeed.global.filter;

import com.wanted.socialMediaIntegratedFeed.global.jwt.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 로그인을 통해 사용자가 받은 JWT를 인증하는 Filter 클래스입니다.
 * @author 정성국
 */
public class JwtFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
            new WebAuthenticationDetailsSource();

    private final BearerResolver bearerResolver = new BearerResolver();

    private final AuthenticationEntryPoint authenticationEntryPoint = new JwtEntryPoint();

    public JwtFilter(AuthenticationManager authenticationManager) {
        Assert.notNull(authenticationManager, "authenticationManager 는 필수입니다.");
        this.authenticationManager = authenticationManager;
    }

    /**
     * HTTP 요청 정보가 저장된 {@link HttpServletRequest} 객체에서 JWT를 추출하여 인증을 수행합니다.
     * JWT가 필요없는 요청의 경우 JWT 인증 작업을 수행하지 않습니다.
     * @author 정성국
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final boolean debug = this.logger.isDebugEnabled();

        String token;

        try {
            token = this.bearerResolver.resolve(request);
        } catch (AuthenticationException invalid) {
            this.authenticationEntryPoint.commence(request, response, invalid);
            return;
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        BearerAuthenticationToken authenticationRequest = new BearerAuthenticationToken(token);

        authenticationRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

        try {
            Authentication authenticationResult = this.authenticationManager.authenticate(authenticationRequest);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationResult);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);
        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            if (debug) {
                this.logger.debug("인증작업 실패: " + failed);
            }

            this.authenticationEntryPoint.commence(request, response, failed);
        }
    }

}
