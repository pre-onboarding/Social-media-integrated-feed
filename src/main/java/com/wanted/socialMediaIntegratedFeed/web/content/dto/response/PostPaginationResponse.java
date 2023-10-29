package com.wanted.socialMediaIntegratedFeed.web.content.dto.response;


import com.wanted.socialMediaIntegratedFeed.domain.post.Post;
import com.wanted.socialMediaIntegratedFeed.domain.post.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPaginationResponse {
    private Long postId;
    private Type type;
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private Long shareCount;
    private List<String> hashtags;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static String ContentCheck(String str) {
        if (str.length() > 20) {
            return str.substring(0, 20);
        }
        return str;
    }

    public static PostPaginationResponse from(Post post, List<String> hashtags) {
        return new PostPaginationResponse(
                post.getId(),
                post.getType(),
                post.getTitle(),
                ContentCheck(post.getContent()),
                post.getViewCount(),
                post.getLikeCount(),
                post.getShareCount(),
                hashtags,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
