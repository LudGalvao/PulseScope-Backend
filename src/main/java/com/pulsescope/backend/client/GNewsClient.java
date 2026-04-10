package com.pulsescope.backend.client;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GNewsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GNewsClient.class);

    private final WebClient externalApiWebClient;
    private final String apiKey;
    private final String language;
    private final String country;
    private final int maxResults;

    public GNewsClient(
            WebClient externalApiWebClient,
            @Value("${integration.external-api.api-key:}") String apiKey,
            @Value("${integration.external-api.language:en}") String language,
            @Value("${integration.external-api.country:us}") String country,
            @Value("${integration.external-api.max-results:10}") int maxResults) {
        this.externalApiWebClient = externalApiWebClient;
        this.apiKey = apiKey;
        this.language = language;
        this.country = country;
        this.maxResults = maxResults;
    }

    public boolean isConfigured() {
        return StringUtils.hasText(apiKey);
    }

    public List<GNewsArticle> searchByKeyword(String keyword) {
        if (!isConfigured()) {
            return Collections.emptyList();
        }

        GNewsResponse response;

        try {
            response = externalApiWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("q", keyword)
                            .queryParam("lang", language)
                            .queryParam("country", country)
                            .queryParam("max", maxResults)
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(GNewsResponse.class)
                    .block();
        } catch (WebClientResponseException exception) {
            LOGGER.warn(
                    "GNews request failed with status {} for keyword '{}': {}",
                    exception.getStatusCode().value(),
                    keyword,
                    exception.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (Exception exception) {
            LOGGER.warn("GNews request failed unexpectedly for keyword '{}'", keyword, exception);
            return Collections.emptyList();
        }

        if (response == null || response.articles() == null) {
            return Collections.emptyList();
        }

        return response.articles();
    }
}
