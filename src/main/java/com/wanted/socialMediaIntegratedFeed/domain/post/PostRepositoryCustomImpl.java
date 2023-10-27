package com.wanted.socialMediaIntegratedFeed.domain.post;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.socialMediaIntegratedFeed.api.content.dto.response.PostPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanted.socialMediaIntegratedFeed.domain.post.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostPaginationResponse> findAllByHashtag(Pageable pageable){

        List<PostPaginationResponse> posts = jpaQueryFactory.select(Projections.fields(PostPaginationResponse.class,
                        post.id.as("postId"),post.title,post.content))
                .from(post)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(post.count())
                .from(post);
        return PageableExecutionUtils.getPage(posts,pageable,countQuery::fetchOne);
    }
}
