package com.pulsescope.backend.dto;

import com.pulsescope.backend.domain.AlertSeverity;
import com.pulsescope.backend.domain.AlertStatus;
import java.time.OffsetDateTime;

public record AlertResponse(
        Long id,
        String title,
        String description,
        AlertSeverity severity,
        AlertStatus status,
        OffsetDateTime createdAt
) {
}
