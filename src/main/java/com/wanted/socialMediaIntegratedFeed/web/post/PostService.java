package com.wanted.socialMediaIntegratedFeed.web.post;

import com.wanted.socialMediaIntegratedFeed.domain.hashtag.HashtagRepository;
import com.wanted.socialMediaIntegratedFeed.domain.post.Post;
import com.wanted.socialMediaIntegratedFeed.domain.post.PostRepository;
import com.wanted.socialMediaIntegratedFeed.global.common.dto.PageResponseDto;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorCode;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorException;
import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostDetailResponse;
import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public void increaseLike(final long id) {
        Post post = findById(id);
        post.increaseLike();
    }

    @Transactional
    public void increaseShare(final long id) {
        Post post = findById(id);
        post.increaseShare();
    }

    private Post findById(final long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_POST));
    }

    @Transactional
    public PostDetailResponse detailPost(Long id){
        Post post =findById(id);
        List<String> hashtagNames = hashtagRepository.getHashtagName(id);
        post.updateView();
        return PostDetailResponse.from(post,hashtagNames);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<PostPaginationResponse> findAllByHashtag(String hashtag, String type, String searchBy, String search, Pageable pageable){

        Long hashtagId = hashtagRepository.findName(hashtag);
        Page<PostPaginationResponse> allByHashtag = postRepository.findAllByHashtag( type, searchBy,  search, pageable,hashtagId);

        return PageResponseDto.of(allByHashtag);
    }
}