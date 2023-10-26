package com.wanted.socialMediaIntegratedFeed.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String errorMessage;
    private final String reason;
}
