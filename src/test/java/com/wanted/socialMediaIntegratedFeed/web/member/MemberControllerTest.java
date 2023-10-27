package com.wanted.socialMediaIntegratedFeed.web.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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

    @DisplayName("테스트에 필요한 Member 저장")
    void before() {
        memberRepository.save(Member.builder()
                        .email("example@gmail.com")
                        .username("example")
                        .password("example12345")
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

}