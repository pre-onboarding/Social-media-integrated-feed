package com.wanted.socialMediaIntegratedFeed.domain.post;

import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostPaginationResponse> findAllByHashtag(String type, String searchBy, String search, Pageable pageable, Long hashtagId) ;
}
