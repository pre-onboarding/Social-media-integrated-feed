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

@RestController
@RequiredArgsConstructor
@Tag(name = "Members", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입 API", responses = {
            @ApiResponse(responseCode = "200")
    })
    @Tag(name = "Members")
    @PostMapping("/api/v1/member")
    public ResponseEntity memberSignup(@Validated @RequestBody SignupRequest request) {

        memberService.memberSignup(request);

        return ResponseEntity.ok().build();
    }
}
