package com.wanted.socialMediaIntegratedFeed.api.content.service;


import com.wanted.socialMediaIntegratedFeed.api.content.dto.response.PostPaginationResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface PostService {
    Page<PostPaginationResponse> findAllByHashtag(String hashtag,String type,String searchBy,String search,Pageable pageable);


}
