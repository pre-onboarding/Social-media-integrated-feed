package com.wanted.socialMediaIntegratedFeed.domain.post;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.socialMediaIntegratedFeed.api.content.dto.response.PostPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanted.socialMediaIntegratedFeed.domain.post.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private OrderSpecifier<?> PostSort(Pageable page) {
        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()){
                    case "created_at":
                        return new OrderSpecifier(direction, post.createdAt);
                    case "updated_at":
                        return new OrderSpecifier(direction, post.modifiedAt);
                    case "like_count":
                        return new OrderSpecifier(direction, post.likeCount);
                    case "share_count":
                        return new OrderSpecifier(direction, post.shareCount);
                    case "view_count":
                        return new OrderSpecifier(direction, post.viewCount);
                }
            }
        }
        return null;
    }
    @Override
    public Page<PostPaginationResponse> findAllByHashtag(Pageable pageable){

        List<PostPaginationResponse> posts = jpaQueryFactory.select(Projections.fields(PostPaginationResponse.class,
                        post.id.as("postId"),post.type,post.title,post.content,
                        post.viewCount,post.likeCount,post.shareCount,post.createdAt,post,post.createdAt,post.modifiedAt))
                .from(post)
                .orderBy(PostSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(post.count())
                .from(post);
        return PageableExecutionUtils.getPage(posts,pageable,countQuery::fetchOne);
    }
}
