package com.wanted.socialMediaIntegratedFeed.web.post.service;

import com.wanted.socialMediaIntegratedFeed.domain.hashtag.HashtagRepository;
import com.wanted.socialMediaIntegratedFeed.global.common.dto.PageResponseDto;
import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostPaginationResponse;

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
    private final HashtagRepository hashtagRepository;

    //
    @Override
    public PageResponseDto<PostPaginationResponse> findAllByHashtag(String hashtag,String type,String searchBy,String search,Pageable pageable){

        Long hashtagId = hashtagRepository.findName(hashtag);
        Page<PostPaginationResponse> allByHashtag = postRepository.findAllByHashtag( type, searchBy,  search, pageable,hashtagId);

        return PageResponseDto.of(allByHashtag);
    }

}
