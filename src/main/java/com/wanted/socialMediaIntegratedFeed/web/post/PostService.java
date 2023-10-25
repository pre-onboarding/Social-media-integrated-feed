package com.wanted.socialMediaIntegratedFeed.web.post;

import com.wanted.socialMediaIntegratedFeed.domain.content.Content;
import com.wanted.socialMediaIntegratedFeed.domain.content.ContentRepository;
import com.wanted.socialMediaIntegratedFeed.global.common.RespnoseStatusValue;
import com.wanted.socialMediaIntegratedFeed.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final ContentRepository contentRepository;

    public void increaseLike(final long id) {
        // JWT에 이메일을 가져와서 해당 member가 있는지 확인 하는 과정 생략
        // 추후 개발 예정
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RespnoseStatusValue.NOT_FOUND_CONTENT));

        content.setLikeCount(content.getLikeCount() + 1);
        contentRepository.save(content);
    }
}
