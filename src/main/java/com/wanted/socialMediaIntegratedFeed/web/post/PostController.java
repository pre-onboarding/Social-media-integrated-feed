package com.wanted.socialMediaIntegratedFeed.web.post;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.global.common.dto.PageResponseDto;
import com.wanted.socialMediaIntegratedFeed.global.common.util.PageableUtil;
import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostDetailResponse;
import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostPaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal Member member,
            @RequestParam(value = "hashtag" ,required = false) String hashtag,
            @RequestParam(value = "type",defaultValue = "",required = false) String type,
            @RequestParam(value = "orderBy",defaultValue = "created_at",required = false) String orderBy,
            @RequestParam(value = "orderBy1",defaultValue = "",required = false) String orderBy1,
            @RequestParam(value = "searchBy",defaultValue = "title,content",required = false) String searchBy,
            @RequestParam(value = "search",defaultValue = "",required = false) String search,
            @RequestParam(value = "pageCount",defaultValue = "10",required = false) int pageCount,
            @RequestParam(value = "page",defaultValue = "0",required = false) int page) {
        if(hashtag == null || hashtag.equals("")){
            hashtag = member.getUsername();
        }

        Pageable pageable = PageableUtil.of(page,pageCount, orderBy,orderBy1);
        PageResponseDto<PostPaginationResponse> responses = postService.findAllByHashtag(hashtag,type,searchBy,search,pageable);

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("{id}")
    public ResponseEntity<PostDetailResponse> detailPost(@PathVariable Long id){
        PostDetailResponse response = postService.detailPost(id);
        return ResponseEntity.ok().body(response);
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