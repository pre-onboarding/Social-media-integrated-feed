package com.wanted.socialMediaIntegratedFeed.web.post;

import com.wanted.socialMediaIntegratedFeed.global.common.RespnoseStatusValue;
import com.wanted.socialMediaIntegratedFeed.global.common.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 좋아요 API
     * @param id
     * @return
     */
    @PatchMapping("/api/v1/content/{id}/like")
    public ResponseResult<RespnoseStatusValue> patchLike(@PathVariable final long id) {
        //String email = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.increaseLike(id);
        return new ResponseResult<>(RespnoseStatusValue.SUCCESS);
    }
}
