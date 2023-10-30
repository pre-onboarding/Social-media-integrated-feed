package com.wanted.socialMediaIntegratedFeed.web.post.dto.response;


import com.wanted.socialMediaIntegratedFeed.domain.post.Post;
import com.wanted.socialMediaIntegratedFeed.domain.post.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {
    @Schema(description = "게시물 id")
    private Long postId;
    @Schema(description = "파일 객체 유형")
    private Type type;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 내용")
    private String content;
    @Schema(description = "조회수")
    private Long viewCount;
    @Schema(description = "좋아요 수")
    private Long likeCount;
    @Schema(description = "공유수")
    private Long shareCount;
    @Schema(description = "해당 게시물 hashtagName")
    private List<String> hashtags;
    @Schema(description = "게시물 작성 기록")
    private LocalDateTime createdAt;
    @Schema(description = "게시물 업데이트 기록")
    private LocalDateTime modifiedAt;




    public static PostDetailResponse from(Post post, List<String> hashtags) {
        return new PostDetailResponse(
                post.getId(),
                post.getType(),
                post.getTitle(),
                post.getContent(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getShareCount(),
                hashtags,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
