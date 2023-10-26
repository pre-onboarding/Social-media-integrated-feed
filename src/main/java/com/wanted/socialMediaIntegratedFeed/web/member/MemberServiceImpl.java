package com.wanted.socialMediaIntegratedFeed.web.member;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorException;
import com.wanted.socialMediaIntegratedFeed.web.member.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wanted.socialMediaIntegratedFeed.global.exception.ErrorCode.DUPLICATE_EMAIL;
import static com.wanted.socialMediaIntegratedFeed.global.exception.ErrorCode.DUPLICATE_USERNAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder encoder;

    /**
     * 멤버 회원가입
     * @param request 에서 받은 이메일, 유저네임이 중복되어 있는지 확인 후 패스워드를 암호화해서 멤버를 저장합니다.
     */
    @Override
    @Transactional
    public void memberSignup(SignupRequest request) {
        emailDuplicateCheck(request.getEmail());
        usernameDuplicateCheck(request.getUsername());

        Member member = Member.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword())).build();

        memberRepository.save(member);
    }

    /**
     * 중복된 이메일 이라면 예외를 던져주고, 그렇지 않다면 false를 리턴합니다.
     */
    private boolean emailDuplicateCheck(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new ErrorException(DUPLICATE_EMAIL);
        }
        return false;
    }

    /**
     * 중복된 유저네임 이라면 예외를 던져주고, 그렇지 않다면 false를 리턴합니다.
     */
    private boolean usernameDuplicateCheck(String name) {
        if (memberRepository.existsByUsername(name)) {
            throw new ErrorException(DUPLICATE_USERNAME);
        }
        return false;
    }
}
