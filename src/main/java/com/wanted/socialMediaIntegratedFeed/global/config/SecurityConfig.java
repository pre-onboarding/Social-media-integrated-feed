package com.wanted.socialMediaIntegratedFeed.global.config;

import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.global.filter.EmailPasswordAuthenticationFilter;
import com.wanted.socialMediaIntegratedFeed.global.filter.JwtFilter;
import com.wanted.socialMediaIntegratedFeed.global.jwt.JwtAuthenticationProvider;
import com.wanted.socialMediaIntegratedFeed.global.jwt.JwtProvider;
import com.wanted.socialMediaIntegratedFeed.web.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 이 클래스는 어플리케이션의 보안 설정을 수행합니다.
 */

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    /**
     * 이메일과 비밀번호를 이용한 로그인 시 사용되는 Filter를 설정합니다.
     * @author 정성국
     */
    @Bean
    @Order(1)
    public SecurityFilterChain signinFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/v1/member/signin")
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(new AntPathRequestMatcher("/api/v1/member/signin", "POST")).permitAll();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(new EmailPasswordAuthenticationFilter(getDaoProviderManager()),
                        RequestCacheAwareFilter.class)
                .build();
    }

    /**
     * 인증 작업에 사용되는 ProviderManager를 반환합니다.
     * @author 정성국
     */
    private ProviderManager getDaoProviderManager() {
        return new ProviderManager(getDaoAuthenticationProvider());
    }

    /**
     * 인증 작업에 사용할 구체적인 Provider를 반환합니다.
     * @author 정성국
     */
    private DaoAuthenticationProvider getDaoAuthenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(new MemberService(memberRepository, jwtProvider));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * JWT 인증을 수행하는 Filter를 설정합니다.
     * @author 정성국
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/v1/**")
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(new AntPathRequestMatcher("/api/v1/member/**", "POST")).permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher("/api/v1/**")).hasRole("MEMBER");
                })
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(new JwtFilter(getJwtProviderManager()),
                        RequestCacheAwareFilter.class)
                .build();
    }

    /**
     * JWT 인증 시 사용할 ProviderManager에 구체적인 Provider를 지정하여 반환합니다.
     * @author 정성국
     */
    private AuthenticationManager getJwtProviderManager() {
        return new ProviderManager(new JwtAuthenticationProvider(memberRepository, jwtProvider));
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().anyRequest();
//    }

    /**
     * 사용자 비밀번호 암호화 작업에 사용할 Encoder를 반환합니다.
     * @author 정성국
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
