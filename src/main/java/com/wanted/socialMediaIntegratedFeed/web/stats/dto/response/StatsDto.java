package com.wanted.socialMediaIntegratedFeed.web.stats.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StatsDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long viewCountSum;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long likeCountSum;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long shareCountSum;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long CountSum;
    private List<PostStats> posts;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PostStats {
        private LocalDate time;
        private Long number;
    }

}