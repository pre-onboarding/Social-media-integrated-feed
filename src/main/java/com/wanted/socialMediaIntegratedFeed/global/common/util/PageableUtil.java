package com.wanted.socialMediaIntegratedFeed.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableUtil {



    public static Pageable of(int oneBasedPage, int size, String orderBy, String orderBy1) {
        Sort sort;
        if(orderBy1.equals("내림차순") ){
            sort = Sort.by(Sort.Direction.ASC, orderBy);
        }
        else{
            sort = Sort.by(Sort.Direction.DESC, orderBy);
        }

        return PageRequest.of(oneBasedPage  , size, sort);
    }
}
