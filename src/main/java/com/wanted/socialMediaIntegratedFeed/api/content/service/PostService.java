package com.wanted.socialMediaIntegratedFeed.api.content.service;


import com.wanted.socialMediaIntegratedFeed.api.content.dto.response.PostPaginationResponse;
import com.wanted.socialMediaIntegratedFeed.domain.hashtag.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    Page<PostPaginationResponse> findAllByHashtag(Pageable pageable);


}
