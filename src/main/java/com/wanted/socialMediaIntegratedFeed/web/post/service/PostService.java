package com.wanted.socialMediaIntegratedFeed.web.post.service;


import com.wanted.socialMediaIntegratedFeed.global.common.dto.PageResponseDto;
import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostPaginationResponse;

import org.springframework.data.domain.Pageable;



public interface PostService {
    PageResponseDto<PostPaginationResponse> findAllByHashtag(String hashtag, String type, String searchBy, String search, Pageable pageable);


}
