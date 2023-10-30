package com.wanted.socialMediaIntegratedFeed.domain.post;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Type {
    FACEBOOK("facebook"),
    TWITTER("twitter"),
    INSTAGRAM("instagram"),
    THREADS("threads");
    @JsonValue
    private final String type;

    Type(String type) {
        this.type = type;
    }
}
