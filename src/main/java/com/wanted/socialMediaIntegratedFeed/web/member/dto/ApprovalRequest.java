package com.wanted.socialMediaIntegratedFeed.web.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequest {

    @Schema(description = "유저네임", example = "example")
    @NotBlank(message = "유저네임을 입력하세요.")
    private String username;

    @Schema(description = "비밀번호", example = "example12345")
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!?@#$%&*]{10,64}$",
            message = "잘못된 비밀번호 형식입니다.")
    private String password;

    @Schema(description = "인증코드", example = "123456")
    @NotBlank(message = "인증코드를 입력하세요.")
    @Pattern(regexp = "^[0-9]*${6}")
    private String authCode;
}
