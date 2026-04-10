package com.pulsescope.backend.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AlphaVantageGlobalQuoteResponse(
        @JsonProperty("Global Quote") AlphaVantageGlobalQuote globalQuote
) {
}
