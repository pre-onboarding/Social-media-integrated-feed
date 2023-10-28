package com.wanted.socialMediaIntegratedFeed.web.content.dto.response;


import com.wanted.socialMediaIntegratedFeed.domain.hashtag.Hashtag;
import com.wanted.socialMediaIntegratedFeed.domain.post.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<Hashtag> hashtags;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
