package com.wanted.socialMediaIntegratedFeed.domain.hashtag;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanted.socialMediaIntegratedFeed.domain.hashtag.QHashtag.hashtag;
import static com.wanted.socialMediaIntegratedFeed.domain.post.QPostHashtag.postHashtag;


@Repository
@RequiredArgsConstructor
public class HashtagRepositoryCustomImpl implements HashtagRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    // hashtagName을 통해 getId
    @Override
    public Long findName(String hashtagName){
        return jpaQueryFactory.select(hashtag.id)
                .from(hashtag)
                .where(hashtag.name.eq("#"+hashtagName))
                .fetchOne();
    }

    // postId을 통해 hashtagName
    @Override
    public List<String> getHashtagName(Long postId) {
        return jpaQueryFactory.select(hashtag.name)
                .from(hashtag)
                .where(postHashtag.post.id.eq(postId))
                .leftJoin(postHashtag)
                .on(hashtag.id.eq(postHashtag.hashtag.id))
                .fetch();
    }
}
