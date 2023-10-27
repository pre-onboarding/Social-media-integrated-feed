package com.wanted.socialMediaIntegratedFeed.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableUtil {



    public static Pageable of(int oneBasedPage, int size, Sort sort) {
        if (oneBasedPage < 1)
            throw new IllegalArgumentException("page는 1 이상이어야 합니다.");

        return PageRequest.of(oneBasedPage - 1 , size, sort);
    }
}
