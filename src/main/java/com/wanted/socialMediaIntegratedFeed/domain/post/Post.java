package com.wanted.socialMediaIntegratedFeed.domain.post;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Entity
@Getter
@Builder
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String type;
    @Column
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Column
    private Long viewCount;
    @Column
    private Long likeCount;
    @Column
    private Long shareCount;

    public void increaseLike() {
        this.likeCount += 1;
    }

    public void increaseShare() {
        this.shareCount += 1;
    }
}
