package com.wanted.socialMediaIntegratedFeed.api.content.dto.response;


import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPaginationResponse{
    private Long postId;
    private String title;
    private String content;
}
