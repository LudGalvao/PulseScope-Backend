package com.pulsescope.backend.dto;

public record DashboardSummaryResponse(
        long totalMentions,
        long positiveMentions,
        long neutralMentions,
        long negativeMentions,
        long openAlerts
) {
}
