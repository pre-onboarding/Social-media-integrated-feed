package com.wanted.socialMediaIntegratedFeed.global.exception;

import com.wanted.socialMediaIntegratedFeed.global.common.RespnoseStatusValue;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private RespnoseStatusValue status;

    public NotFoundException(RespnoseStatusValue status) {
        super(status.getMessage());
        this.status = status;
    }
}
