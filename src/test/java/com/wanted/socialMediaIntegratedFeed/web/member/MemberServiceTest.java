package com.wanted.socialMediaIntegratedFeed.web.member;

import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder encoder;

    @Transactional
    @Test
    @DisplayName("Member 회원가입 성공")
    void memberSignup() {
        // given
        SignupRequest request = new SignupRequest("example@gmail.com", "example", "example12345");
        // when
        // then
        memberService.memberSignup(request);
    }

    @Transactional
    @Test
    @DisplayName("Member 회원가입 실패")
    void memberSignupFail() {
        /**
         * 중복된 이메일로 인한 실패
         */
        // given
        SignupRequest request = new SignupRequest("example@gmail.com", "example", "example12345");
        // when
        // stub
        when(memberRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // then
        assertThatThrownBy(() -> memberService.memberSignup(request)).hasMessage("중복된 이메일이 있습니다.");

        /**
         * 중복된 유저네임으로 인한 실패
         */
        // given
        SignupRequest request2 = new SignupRequest("example1@gmail.com", "example1", "example12345");
        // when
        // stub
        when(memberRepository.existsByUsername(request2.getUsername())).thenReturn(true);

        // then
        assertThatThrownBy(() -> memberService.memberSignup(request2)).hasMessage("중복된 유저네임이 있습니다.");
    }
}