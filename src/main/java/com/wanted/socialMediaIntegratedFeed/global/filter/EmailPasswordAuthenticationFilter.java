package com.wanted.socialMediaIntegratedFeed.global.filter;

import com.wanted.socialMediaIntegratedFeed.global.converter.EmailPasswordAuthenticationConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 이메일과 비밀번호를 이용한 로그인 수행 시 인증작업을 수행하는 Filter 클래스입니다.
 * @author 정성국
 */
public class EmailPasswordAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * HTTP 요청 정보가 저장된 {@link HttpServletRequest} 객체에서 사용자가 입력한 이메일과 비밀번호를 추출하고 인증을 요청합니다.
     * @author 정성국
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        UsernamePasswordAuthenticationToken token = new EmailPasswordAuthenticationConverter().convert(request);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (token == null) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            String jsonResponse = "{\"error\": \"이메일 및 비밀번호 형식을 확인해주세요.\"}";
            response.getWriter().write(jsonResponse);
            return;
        }

        try {
            Authentication authenticationResult = this.authenticationManager.authenticate(token);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationResult);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            String jsonResponse = "{\"error\": \"가입승인되지 않았거나 존재하지 않는 회원입니다.\"}";
            response.getWriter().write(jsonResponse);
        }
    }

}
