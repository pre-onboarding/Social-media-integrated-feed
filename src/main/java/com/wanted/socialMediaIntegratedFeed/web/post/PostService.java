package com.wanted.socialMediaIntegratedFeed.web.post;

import com.wanted.socialMediaIntegratedFeed.domain.post.Post;
import com.wanted.socialMediaIntegratedFeed.domain.post.PostRepository;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorCode;
import com.wanted.socialMediaIntegratedFeed.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void increaseLike(final long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_POST));
        post.increaseLike();
    }

    @Transactional
    public void increaseShare(final long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_POST));
        post.increaseShare();
    }
}
