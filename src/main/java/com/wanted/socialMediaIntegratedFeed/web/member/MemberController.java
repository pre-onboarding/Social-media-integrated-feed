package com.wanted.socialMediaIntegratedFeed.web.member;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 사용자로부터 이메일과 비밀번호를 받아 인증이 완료된 경우 Access Token을 반환합니다.
     * @param member 인증된 사용자 정보
     * @return 로그인 성공 시 Access Token을 반환합니다.
     * @author 정성국
     */
    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> signin(@AuthenticationPrincipal Member member) {
        String accessToken = memberService.signin(member);
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

}
