package com.pulsescope.backend.dto;

import com.pulsescope.backend.domain.Sentiment;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record MentionResponse(
        Long id,
        String keyword,
        String source,
        String author,
        String title,
        String content,
        String sourceUrl,
        OffsetDateTime publishedAt,
        OffsetDateTime collectedAt,
        Sentiment sentiment,
        BigDecimal reputationScore,
        Boolean trending
) {
}
