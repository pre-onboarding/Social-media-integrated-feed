package com.wanted.socialMediaIntegratedFeed.web.post;

import com.wanted.socialMediaIntegratedFeed.global.common.dto.PageResponseDto;
import com.wanted.socialMediaIntegratedFeed.global.common.util.PageableUtil;
import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostPaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post Controller",description = "Post API")
public class PostController {

    private final PostService postService;
    @Operation(summary = "게시물 목록 api")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<PageResponseDto<PostPaginationResponse>> findAll(
            @RequestParam("hashtag") String hashtag,
            @RequestParam("type") String type,
            @RequestParam(value = "orderBy",defaultValue = "created_at") String orderBy,
            @RequestParam("orderBy1") String orderBy1,
            @RequestParam(value = "searchBy",defaultValue = "title,content") String searchBy,
            @RequestParam("search") String search,
            @RequestParam(value = "pageCount",defaultValue = "10") int pageCount,
            @RequestParam(value = "page",defaultValue = "0") int page) {
        Sort sort;
        if(orderBy1.equals("내림차순") ){
            sort = Sort.by(Sort.Direction.ASC, orderBy);
        }
        else{
            sort = Sort.by(Sort.Direction.DESC, orderBy);
        }
        Pageable pageable = PageableUtil.of(page,pageCount, sort);
        PageResponseDto<PostPaginationResponse> responses = postService.findAllByHashtag(hashtag,type,searchBy,search,pageable);

        return ResponseEntity.ok().body(responses);
    }

    @PatchMapping("/{id}/like")
    public ResponseEntity patchLike(@PathVariable final long id) {
        postService.increaseLike(id);
        return ResponseEntity.ok().build();
    }
     
    @PatchMapping("/{id}/share")
    public ResponseEntity patchShare(@PathVariable final long id) {
        postService.increaseShare(id);
        return ResponseEntity.ok().build();
    }
}