package com.wanted.socialMediaIntegratedFeed.domain.post;

import com.wanted.socialMediaIntegratedFeed.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@Builder
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @Column
    @Enumerated(EnumType.STRING)
    private Type type;
  
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
