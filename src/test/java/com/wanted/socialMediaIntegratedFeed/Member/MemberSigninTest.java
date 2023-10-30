package com.wanted.socialMediaIntegratedFeed.Member;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.global.config.JwtConfig;
import com.wanted.socialMediaIntegratedFeed.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberSigninTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    JwtConfig jwtConfig;

    static Member member;

    @BeforeAll
    static void init() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member = Member.builder()
                .auth(true)
                .username("wanted")
                .email("wanted@wanted.com")
                .password(passwordEncoder.encode("passwordpassword"))
                .build();
    }

    @Test
    @DisplayName("회원가입 및 승인된 사용자가 로그인 요청")
    void shouldCreateANewTokenIfUserExist() {

        memberRepository.save(member);

        Member loginRequestedMember = Member.builder()
                .email("wanted@wanted.com")
                .password("passwordpassword")
                .build();

        ResponseEntity<String> createResponse = restTemplate
                .postForEntity("/api/v1/member/signin", loginRequestedMember, String.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Response Body 확인
        DocumentContext documentContext = JsonPath.parse(createResponse.getBody());
        String token = documentContext.read("$.accessToken");

        // JWT claims 확인
        Claims claims = jwtProvider.parse(token).getPayload();
        assertThat(claims.getIssuer()).isEqualTo(jwtConfig.getJwtIssuer());
        assertThat(Long.parseLong(claims.getSubject())).isEqualTo(1L);
        assertThat(claims.getIssuedAt()).isBefore(claims.getExpiration());
        assertThat(claims.getIssuedAt()).isBefore(new Date());

    }
}
