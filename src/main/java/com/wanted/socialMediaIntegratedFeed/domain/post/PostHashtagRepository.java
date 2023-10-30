package com.wanted.socialMediaIntegratedFeed.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

    @Query("SELECT ph FROM PostHashtag ph " +
            "WHERE ph.hashtag.name = :hashtagName AND ph.post.createdAt BETWEEN :start AND :end")
    List<PostHashtag> findByHashtagNameAndPostCreatedBetween(@Param("hashtagName") String hashtagName,
                                                      @Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);
}
