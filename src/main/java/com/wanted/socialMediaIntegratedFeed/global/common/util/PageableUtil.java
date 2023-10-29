package com.wanted.socialMediaIntegratedFeed.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableUtil {



    public static Pageable of(int oneBasedPage, int size, Sort sort) {


        return PageRequest.of(oneBasedPage  , size, sort);
    }
}
