package com.wanted.socialMediaIntegratedFeed.web.member;

import com.wanted.socialMediaIntegratedFeed.web.member.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "Members", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입 API", responses = {
            @ApiResponse(responseCode = "200")
    })
    @Tag(name = "Members")
    @PostMapping
    public ResponseEntity memberSignup(@Validated @RequestBody SignupRequest request) {

        memberService.memberSignup(request);

        return ResponseEntity.ok().build();
    }

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
