package com.wanted.socialMediaIntegratedFeed.domain.hashtag;


import java.util.List;

public interface HashtagRepositoryCustom {
    Long findName(String hashtagName);
    List<String> getHashtagName(Long postId);
}
