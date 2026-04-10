package com.pulsescope.backend.dto;

import java.math.BigDecimal;

public record MarketQuoteResponse(
        String symbol,
        BigDecimal open,
        BigDecimal high,
        BigDecimal low,
        BigDecimal price,
        Long volume,
        String latestTradingDay,
        BigDecimal previousClose,
        BigDecimal change,
        String changePercent
) {
}
