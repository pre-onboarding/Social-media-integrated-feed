package com.wanted.socialMediaIntegratedFeed.domain.post;

import com.wanted.socialMediaIntegratedFeed.web.content.dto.response.PostPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostPaginationResponse> findAllByHashtag(String hashtag,String type,String searchBy,String search,Pageable pageable);
}
