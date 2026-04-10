package com.pulsescope.backend.service;

import com.pulsescope.backend.client.AlphaVantageClient;
import com.pulsescope.backend.client.AlphaVantageGlobalQuote;
import com.pulsescope.backend.client.AlphaVantageGlobalQuoteResponse;
import com.pulsescope.backend.dto.MarketQuoteResponse;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MarketService {

    private final AlphaVantageClient alphaVantageClient;

    public MarketService(AlphaVantageClient alphaVantageClient) {
        this.alphaVantageClient = alphaVantageClient;
    }

    public MarketQuoteResponse getQuote(String symbol) {
        if (!alphaVantageClient.isConfigured()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Alpha Vantage API key is not configured");
        }

        AlphaVantageGlobalQuoteResponse response = alphaVantageClient.getGlobalQuote(symbol);

        if (response == null || response.globalQuote() == null || !StringUtils.hasText(response.globalQuote().symbol())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No market data found for symbol " + symbol);
        }

        AlphaVantageGlobalQuote quote = response.globalQuote();

        return new MarketQuoteResponse(
                quote.symbol(),
                toBigDecimal(quote.open()),
                toBigDecimal(quote.high()),
                toBigDecimal(quote.low()),
                toBigDecimal(quote.price()),
                toLong(quote.volume()),
                quote.latestTradingDay(),
                toBigDecimal(quote.previousClose()),
                toBigDecimal(quote.change()),
                quote.changePercent()
        );
    }

    private BigDecimal toBigDecimal(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return new BigDecimal(value);
    }

    private Long toLong(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return Long.valueOf(value);
    }
}
