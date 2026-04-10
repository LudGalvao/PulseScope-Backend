package com.pulsescope.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient externalApiWebClient(
            WebClient.Builder builder,
            @Value("${integration.external-api.base-url:http://localhost:8081}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean
    @Qualifier("alphaVantageWebClient")
    public WebClient alphaVantageWebClient(
            WebClient.Builder builder,
            @Value("${integration.alpha-vantage.base-url:https://www.alphavantage.co/query}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean
    public WebClient analysisApiWebClient(
            WebClient.Builder builder,
            @Value("${integration.analysis-api.base-url:http://localhost:8082}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
