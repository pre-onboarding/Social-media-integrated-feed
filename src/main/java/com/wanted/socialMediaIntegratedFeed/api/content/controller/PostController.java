package com.wanted.socialMediaIntegratedFeed.api.content.controller;

import com.wanted.socialMediaIntegratedFeed.api.content.dto.response.PostPaginationResponse;
import com.wanted.socialMediaIntegratedFeed.api.content.service.PostService;
import com.wanted.socialMediaIntegratedFeed.domain.hashtag.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostPaginationResponse>> findAll(Pageable pageable) {

        Page<PostPaginationResponse> responses = postService.findAllByHashtag(pageable);

        return ResponseEntity.ok().body(responses);
    }

}
