package com.wanted.socialMediaIntegratedFeed.api.content.dto.response;


import com.wanted.socialMediaIntegratedFeed.domain.post.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPaginationResponse{
    private Long postId;
    private Type type;
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private Long shareCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
