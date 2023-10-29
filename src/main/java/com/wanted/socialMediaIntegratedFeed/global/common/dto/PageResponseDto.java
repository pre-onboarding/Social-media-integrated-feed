package com.wanted.socialMediaIntegratedFeed.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {
    private List<T> content;
    private int pageCount;
    private int page;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private boolean hasNext;
    private boolean hasPrevious;

    public static <T> PageResponseDto<T> of(Page<T> page){
        return new PageResponseDto<T>(
                page.getContent(),
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
