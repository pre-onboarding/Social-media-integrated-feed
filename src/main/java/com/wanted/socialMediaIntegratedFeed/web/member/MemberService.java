package com.wanted.socialMediaIntegratedFeed.web.member;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.domain.member.MemberRepository;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorCode;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorException;
import com.wanted.socialMediaIntegratedFeed.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public String signin(Member member) {
        memberRepository.updateRefreshToken(member.getId(), jwtProvider.generateRefreshToken(member));
        return jwtProvider.generateAccessToken(member);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException("이메일 혹은 비밀번호가 일치하지 않습니다.", ErrorCode.NOT_FOUND_MEMBER));
    }

}
