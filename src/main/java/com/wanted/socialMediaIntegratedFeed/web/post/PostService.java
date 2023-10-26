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
        // JWT에 이메일을 가져와서 해당 member가 있는지 확인 하는 과정 생략
        // 추후 개발 예정
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_POST));
        post.increaseLike();
    }
}
