package com.wanted.socialMediaIntegratedFeed.api.content.service;

import com.wanted.socialMediaIntegratedFeed.api.content.dto.response.PostPaginationResponse;
import com.wanted.socialMediaIntegratedFeed.domain.post.Post;
import com.wanted.socialMediaIntegratedFeed.domain.post.PostRepository;
import com.wanted.socialMediaIntegratedFeed.domain.hashtag.Hashtag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    @Override
    public Page<PostPaginationResponse> findAllByHashtag(Pageable pageable){
        return postRepository.findAllByHashtag(pageable);
    }

}
