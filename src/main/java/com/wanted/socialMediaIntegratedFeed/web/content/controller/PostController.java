package com.wanted.socialMediaIntegratedFeed.web.content.controller;

import com.wanted.socialMediaIntegratedFeed.web.content.dto.response.PostPaginationResponse;
import com.wanted.socialMediaIntegratedFeed.web.content.service.PostService;
import com.wanted.socialMediaIntegratedFeed.global.common.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<PostPaginationResponse>> findAll(
            @RequestParam("hashtag") String hashtag,
            @RequestParam("type") String type,
            @RequestParam("orderBy") String orderBy,
            @RequestParam("orderBy1") String orderBy1,
            @RequestParam("searchBy") String searchBy,
            @RequestParam("search") String search,
            @RequestParam("pageCount") int pageCount,
            @RequestParam("page") int page) {
        Sort sort;
        if(orderBy1.equals("내림차순") ){
            sort = Sort.by(Sort.Direction.ASC, orderBy);
        }
        else{
            sort = Sort.by(Sort.Direction.DESC, orderBy);
        }
        Pageable pageable = PageableUtil.of(pageCount,page, sort);

        Page<PostPaginationResponse> responses = postService.findAllByHashtag(hashtag,type,searchBy,search,pageable);

        return ResponseEntity.ok().body(responses);
    }

}
