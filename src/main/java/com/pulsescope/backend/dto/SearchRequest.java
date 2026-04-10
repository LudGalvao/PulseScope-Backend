package com.pulsescope.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SearchRequest(
        @NotBlank(message = "Keyword is required")
        @Size(min = 2, max = 100, message = "Keyword must have between 2 and 100 characters")
        String keyword
) {
}
