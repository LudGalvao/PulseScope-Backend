package com.pulsescope.backend.dto;

import com.pulsescope.backend.domain.AlertStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAlertStatusRequest(
        @NotNull(message = "Status is required")
        AlertStatus status
) {
}
