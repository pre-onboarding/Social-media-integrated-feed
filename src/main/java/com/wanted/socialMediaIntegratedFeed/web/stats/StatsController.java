package com.wanted.socialMediaIntegratedFeed.web.stats;

import com.wanted.socialMediaIntegratedFeed.domain.member.Member;
import com.wanted.socialMediaIntegratedFeed.web.stats.dto.response.StatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/api/v1/stat")
    public StatsDto getStats(@RequestParam(required = false) String hashtag,
                             @RequestParam String type,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime start,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime end,
                             @RequestParam(defaultValue = "count") String value,
                             @AuthenticationPrincipal Member member) {
        return statsService.getStats(hashtag, type, start, end, value, member.getUsername());
    }
}
