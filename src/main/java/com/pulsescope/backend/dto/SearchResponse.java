package com.pulsescope.backend.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record SearchResponse(
        Long searchId,
        String keyword,
        Integer resultsFound,
        OffsetDateTime searchedAt,
        List<MentionResponse> mentions
) {
}
