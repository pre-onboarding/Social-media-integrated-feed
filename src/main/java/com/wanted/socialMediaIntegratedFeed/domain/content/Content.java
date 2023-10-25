package com.wanted.socialMediaIntegratedFeed.domain.content;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Content {

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

}
