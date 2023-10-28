package com.wanted.socialMediaIntegratedFeed.web.member;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.ApprovalRequest;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private RedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Transactional
    @Test
    @DisplayName("Member 회원가입 성공")
    void memberSignup() {
        // given
        Member member = new Member(1L, "example@gmail.com", "example", "example12345", null, false);
        SignupRequest request = new SignupRequest("example@gmail.com", "example", "example12345");
        // when
        // stub
        when(memberRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(member));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

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

    @Transactional
    @Test
    @DisplayName("멤버 인증 성공")
    void memberApproval() {
        // given
        Member member = new Member(1L, "example@gmail.com", "example", "example12345", null, false);
        ApprovalRequest request = new ApprovalRequest("example", "example12345", "123456");

        // stub
        when(memberRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(member));
        when(encoder.matches(request.getPassword(), member.getPassword())).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("AuthCode " + request.getUsername())).thenReturn("123456");

        // when
        // then
        memberService.memberApproval(request);
    }

    @Transactional
    @Test
    @DisplayName("멤버 인증 실패")
    void memberApprovalFail() {
        // given
        Member member = new Member(1L, "example@gmail.com", "example", "example12345", null, false);
        ApprovalRequest request = new ApprovalRequest("example", "example12345", "123456");

        // stub
        when(memberRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(member));
        when(encoder.matches(request.getPassword(), member.getPassword())).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("AuthCode " + request.getUsername())).thenReturn("123451");

        // when
        // then
        assertThatThrownBy(() -> memberService.memberApproval(request)).hasMessage("올바르지 않는 인증코드입니다.");
    }
}