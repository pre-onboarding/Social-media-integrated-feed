package com.wanted.socialMediaIntegratedFeed.domain.post;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.socialMediaIntegratedFeed.web.content.dto.response.PostPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.wanted.socialMediaIntegratedFeed.domain.post.QPost.post;
import static com.wanted.socialMediaIntegratedFeed.domain.post.QPostHashtag.postHashtag;


@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<PostPaginationResponse> findAllByHashtag(String hashtag, String type, String searchBy, String search, Pageable pageable) {

        List<PostPaginationResponse> posts = jpaQueryFactory.select(Projections.fields(PostPaginationResponse.class,
                        post.id.as("postId"), post.type, post.title, post.content,
                        post.viewCount, post.likeCount, post.shareCount,postHashtag.hashtag,
                        post.createdAt, post, post.createdAt, post.modifiedAt))
                .from(post)
                .leftJoin(postHashtag)
                .on(post.id.eq(postHashtag.post.id))
                .orderBy(PostSort(pageable))
                .where(
                        searchPost(searchBy, search),
                        searchHashtag(hashtag),
                        post.type.stringValue().contains(type)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(
                        searchPost(searchBy, search),
                        searchHashtag(hashtag)
                );
        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }


    private OrderSpecifier<?> PostSort(Pageable page) {
        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
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


    private BooleanExpression searchPost(String searchBy, String search) {
        if (!searchBy.isEmpty()) {
            switch (searchBy) {
                case "title":
                    return post.title.contains(search);
                case "content":
                    return post.content.contains(search);
                case "title,content":
                    return post.content.contains(search).or(post.title.contains(search));
            }
        }
        return null;
    }

    private BooleanExpression searchHashtag(String hashtag) {
        if (ObjectUtils.isEmpty(hashtag) || hashtag == null)
            return null;
        return postHashtag.hashtag.name.eq("#" + hashtag);
    }
}
