package com.pulsescope.backend.client;

import java.util.List;

public record GNewsResponse(
        Integer totalArticles,
        List<GNewsArticle> articles
) {
}
