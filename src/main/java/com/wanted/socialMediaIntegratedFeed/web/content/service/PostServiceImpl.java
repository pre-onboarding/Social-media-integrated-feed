package com.wanted.socialMediaIntegratedFeed.web.content.service;

import com.wanted.socialMediaIntegratedFeed.web.content.dto.response.PostPaginationResponse;

import com.wanted.socialMediaIntegratedFeed.domain.post.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    @Override
    public Page<PostPaginationResponse> findAllByHashtag(String hashtag,String type,String searchBy,String search,Pageable pageable){
        return postRepository.findAllByHashtag(hashtag,type,searchBy,search,pageable);
    }

}
