package com.wanted.socialMediaIntegratedFeed.web.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.ApprovalRequest;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @DisplayName("테스트에 필요한 Member 저장")
    void before() {
        memberRepository.save(Member.builder()
                        .email("example@gmail.com")
                        .username("example")
                        .password(encoder.encode("example12345"))
                        .build());
    }

    @Transactional
    @Test
    @DisplayName("Member 회원가입 성공")
    void memberSignup() throws Exception {
        // given
        SignupRequest request = new SignupRequest("example@gmail.com", "example", "example12345");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/v1/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Transactional
    @Test
    @DisplayName("Member 회원가입 실패")
    void memberSignupFail() throws Exception {
        // given
        before(); // 테스트에 필요 Member 저장
        /**
         * 중복된 유저네임으로 인한 실패
         */
        SignupRequest request = new SignupRequest("example1@gmail.com", "example", "example12345");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/v1/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        /**
         * 중복된 이메일로 인한 실패
         */
        // given
        SignupRequest request2 = new SignupRequest("example@gmail.com", "example1", "example12345");
        String content2 = new ObjectMapper().writeValueAsString(request2);

        // when
        ResultActions resultActions2 = mvc.perform(post("/api/v1/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content2)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("errorCode").value("DUPLICATE_USERNAME"))
                .andExpect(jsonPath("reason").value("중복된 유저네임이 있습니다."));

        resultActions2.andExpect(status().isConflict())
                .andExpect(jsonPath("errorCode").value("DUPLICATE_EMAIL"))
                .andExpect(jsonPath("reason").value("중복된 이메일이 있습니다."));
    }

    @Transactional
    @Test
    @DisplayName("멤버 인증 성공")
    void memberApproval() throws Exception {
        // given
        before(); // 테스트에 필요 Member 저장
        valueOperations = redisTemplate.opsForValue();
        valueOperations.set("AuthCode example" , "123456", 5, TimeUnit.MINUTES);
        ApprovalRequest request = new ApprovalRequest("example", "example12345", "123456");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/v1/member/approval")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Transactional
    @Test
    @DisplayName("멤버 인증 실패")
    void memberApprovalFail() throws Exception {
        // given
        before(); // 테스트에 필요 Member 저장
        valueOperations = redisTemplate.opsForValue();
        valueOperations.set("AuthCode example" , "123452", 5, TimeUnit.MINUTES);
        ApprovalRequest request = new ApprovalRequest("example", "example12345", "123456");
        String content = new ObjectMapper().writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/v1/member/approval")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value("WRONG_AUTH_CODE"))
                .andExpect(jsonPath("reason").value("올바르지 않는 인증코드입니다."));
    }

    @Transactional
    @Test
    @DisplayName("인증코드 재전송 성공")
    void reSendAuthCode() throws Exception {
        // given
        before(); // 테스트에 필요 Member 저장
        String email = "example@gmail.com";
        String username = "example";

        // when
        ResultActions resultActions = mvc.perform(post("/api/v1/member/re-send/auth-code?" +
                "email=" + email + "&username=" + username));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Transactional
    @Test
    @DisplayName("인증코드 재전송 실패")
    void reSendAuthCodeFail() throws Exception {
        // given
        before(); // 테스트에 필요 Member 저장
        String email = "example2@gmail.com";
        String username = "example";

        // when
        /** 이메일로 멤버를 찾지 못할 경우. */
        ResultActions resultActions = mvc.perform(post("/api/v1/member/re-send/auth-code?" +
                "email=" + email + "&username=" + username));

        /** 이메일로 찾은 멤버의 유저네임과 다를 경우 */
        email = "example@gmail.com";
        username = "example2";
        ResultActions resultActions2 = mvc.perform(post("/api/v1/member/re-send/auth-code?" +
                "email=" + email + "&username=" + username));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value("NON_EXISTENT_MEMBER"))
                .andExpect(jsonPath("reason").value("존재하지 않는 멤버입니다."));

        resultActions2.andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value("WRONG_USERNAME"))
                .andExpect(jsonPath("reason").value("올바르지 않는 유저네임입니다."));
    }

}