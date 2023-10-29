package com.wanted.socialMediaIntegratedFeed.domain.post;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.socialMediaIntegratedFeed.domain.hashtag.HashtagRepository;
import com.wanted.socialMediaIntegratedFeed.web.content.dto.response.PostPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.wanted.socialMediaIntegratedFeed.domain.hashtag.QHashtag.hashtag;
import static com.wanted.socialMediaIntegratedFeed.domain.post.QPost.post;
import static com.wanted.socialMediaIntegratedFeed.domain.post.QPostHashtag.postHashtag;


@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final HashtagRepository hashtagRepository;


    @Override
    public Page<PostPaginationResponse> findAllByHashtag(String type, String searchBy, String search, Pageable pageable, Long hashtagId) {

        List<Post> posts = jpaQueryFactory.selectFrom(post)
                .orderBy(PostSort(pageable))
                .where(
                        searchPost(searchBy, search),
                        searchHashtag(hashtagId),
                        post.type.stringValue().contains(type)
                )
                .leftJoin(postHashtag)
                .on(post.id.eq(postHashtag.post.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(post.count())
                .from(post)
                .leftJoin(postHashtag)
                .on(post.id.eq(postHashtag.post.id))
                .where(
                        searchPost(searchBy, search),
                        searchHashtag(hashtagId),
                        post.type.stringValue().contains(type)
                );


        List<PostPaginationResponse> responses = new ArrayList<>();
        for(Post post1 : posts){
            List<String> hashtagNames = hashtagRepository.getHashtagName(post1.getId());
            PostPaginationResponse from = PostPaginationResponse.from(post1,hashtagNames);
            responses.add(from);
        }

        return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
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

    private BooleanExpression searchHashtag(Long hashtagId) {
        if (ObjectUtils.isEmpty(hashtag) || hashtag == null)
            return null;
        return postHashtag.hashtag.id.eq(hashtagId);
    }
}
