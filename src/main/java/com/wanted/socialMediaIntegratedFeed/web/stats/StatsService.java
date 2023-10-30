package com.wanted.socialMediaIntegratedFeed.web.stats;

import com.wanted.socialMediaIntegratedFeed.domain.post.PostHashtag;
import com.wanted.socialMediaIntegratedFeed.domain.post.PostHashtagRepository;
import com.wanted.socialMediaIntegratedFeed.web.stats.dto.response.StatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final PostHashtagRepository postHashtagRepository;

    public StatsDto getStats(String hashtag, String type, LocalDateTime start, LocalDateTime end, String value, String username) {
        initializeDefaults(hashtag, type, start, end, value, username);
        List<PostHashtag> posts = postHashtagRepository.findByHashtagNameAndPostCreatedBetween(hashtag, start, end);

        StatsDto statsDto = new StatsDto();

        setCountsInStatsDto(statsDto, posts, value);

        if (value.equals("date"))
            setDailyStats(posts, statsDto);
        else setHourlyStats(posts, start, end, statsDto);

        return statsDto;
    }

    private void initializeDefaults(String hashtag, String type, LocalDateTime start, LocalDateTime end, String value, String username) {
        hashtag = defaultIfNull(hashtag, username);
        start = determineStartDateTime(type, start);
        end = defaultIfNull(end, LocalDateTime.now());
    }

    private <T> T defaultIfNull(T value, T defaultValue) {
        if (value != null)
            return value;
        return defaultValue;
    }

    private LocalDateTime determineStartDateTime(String type, LocalDateTime start) {
        if (type.equals("date"))
            return defaultIfNull(start, LocalDateTime.now().minusDays(30));
        return defaultIfNull(start, LocalDateTime.now().minusDays(7));
    }

    private void setCountsInStatsDto(StatsDto statsDto, List<PostHashtag> posts, String value) {
        long totalCount;
        switch (value) {
            case "view_count" -> {
                totalCount = posts.stream()
                        .mapToLong(ph -> ph.getPost().getViewCount())
                        .sum();
                statsDto.setViewCountSum(totalCount);
            }
            case "like_count" -> {
                totalCount = posts.stream()
                        .mapToLong(ph -> ph.getPost().getLikeCount())
                        .sum();
                statsDto.setLikeCountSum(totalCount);
            }
            case "share_count" -> {
                totalCount = posts.stream()
                        .mapToLong(ph -> ph.getPost().getLikeCount())
                        .sum();
                statsDto.setShareCountSum(totalCount);
            }
            default -> statsDto.setCountSum((long) posts.size());
        }
    }

    private void setDailyStats(List<PostHashtag> posts, StatsDto statsDto) {
        Map<LocalDate, Long> dailyPostCounts = posts.stream()
                .collect(Collectors.groupingBy(postHashtag -> postHashtag.getCreatedAt().toLocalDate(), Collectors.counting()));

        List<StatsDto.PostStats> postStatsList = dailyPostCounts.entrySet().stream()
                .map(entry -> new StatsDto.PostStats(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(StatsDto.PostStats::getTime))
                .toList();

        statsDto.setPosts(postStatsList);
    }

    private void setHourlyStats(List<PostHashtag> posts, LocalDateTime start, LocalDateTime end, StatsDto statsDto) {
        Map<LocalDateTime, Long> hourlyPostCounts = posts.stream()
                .collect(Collectors.groupingBy(
                        postHashtag -> postHashtag.getCreatedAt().truncatedTo(ChronoUnit.HOURS),
                        Collectors.counting()
                ));

        List<StatsDto.PostStats> postStatsList = new ArrayList<>();
        LocalDateTime current = start.withHour(0).withMinute(0).withSecond(0);
        while(!current.isAfter(end)) {
            Long count = hourlyPostCounts.getOrDefault(current, 0L);
            postStatsList.add(new StatsDto.PostStats(current.toLocalDate(), count));
            current = current.plusHours(1);
        }
        statsDto.setPosts(postStatsList);
    }
}
