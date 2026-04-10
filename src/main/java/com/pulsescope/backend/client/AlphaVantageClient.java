package com.pulsescope.backend.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AlphaVantageClient {

    private final WebClient alphaVantageWebClient;
    private final String apiKey;

    public AlphaVantageClient(
            @Qualifier("alphaVantageWebClient") WebClient alphaVantageWebClient,
            @Value("${integration.alpha-vantage.api-key:}") String apiKey) {
        this.alphaVantageWebClient = alphaVantageWebClient;
        this.apiKey = apiKey;
    }

    public boolean isConfigured() {
        return StringUtils.hasText(apiKey);
    }

    public AlphaVantageGlobalQuoteResponse getGlobalQuote(String symbol) {
        return alphaVantageWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "GLOBAL_QUOTE")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(AlphaVantageGlobalQuoteResponse.class)
                .block();
    }
}
