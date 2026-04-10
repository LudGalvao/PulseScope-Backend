package com.pulsescope.backend.client;

public record GNewsArticle(
        String title,
        String description,
        String content,
        String url,
        String image,
        String publishedAt,
        GNewsSource source
) {
}
