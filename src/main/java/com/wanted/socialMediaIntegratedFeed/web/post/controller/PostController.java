package com.wanted.socialMediaIntegratedFeed.web.post.controller;

import com.wanted.socialMediaIntegratedFeed.global.common.dto.PageResponseDto;
import com.wanted.socialMediaIntegratedFeed.global.common.util.PageableUtil;
import com.wanted.socialMediaIntegratedFeed.web.post.dto.response.PostPaginationResponse;
import com.wanted.socialMediaIntegratedFeed.web.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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

}
