package com.pulsescope.backend.service;

import com.pulsescope.backend.client.GNewsArticle;
import com.pulsescope.backend.client.GNewsClient;
import com.pulsescope.backend.domain.AnalysisResult;
import com.pulsescope.backend.domain.Alert;
import com.pulsescope.backend.domain.AlertSeverity;
import com.pulsescope.backend.domain.AlertStatus;
import com.pulsescope.backend.domain.Mention;
import com.pulsescope.backend.domain.SearchHistory;
import com.pulsescope.backend.domain.Sentiment;
import com.pulsescope.backend.dto.MentionResponse;
import com.pulsescope.backend.dto.SearchRequest;
import com.pulsescope.backend.dto.SearchResponse;
import com.pulsescope.backend.repository.AlertRepository;
import com.pulsescope.backend.repository.AnalysisResultRepository;
import com.pulsescope.backend.repository.MentionRepository;
import com.pulsescope.backend.repository.SearchHistoryRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class SearchService {

    private static final BigDecimal DEFAULT_REPUTATION_SCORE = BigDecimal.valueOf(72.50);

    private final GNewsClient gNewsClient;
    private final MentionRepository mentionRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final AlertRepository alertRepository;

    public SearchService(
            GNewsClient gNewsClient,
            MentionRepository mentionRepository,
            AnalysisResultRepository analysisResultRepository,
            SearchHistoryRepository searchHistoryRepository,
            AlertRepository alertRepository) {
        this.gNewsClient = gNewsClient;
        this.mentionRepository = mentionRepository;
        this.analysisResultRepository = analysisResultRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.alertRepository = alertRepository;
    }

    @Transactional
    public SearchResponse search(SearchRequest request) {
        OffsetDateTime now = OffsetDateTime.now();
        List<MentionResponse> mentionResponses = gNewsClient.isConfigured()
                ? searchWithGNews(request.keyword(), now)
                : createFallbackMentions(request.keyword(), now);

        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setKeyword(request.keyword());
        searchHistory.setResultsFound(mentionResponses.size());
        searchHistory.setSearchedAt(now);
        SearchHistory savedSearch = searchHistoryRepository.save(searchHistory);
        createAlertsForSearch(savedSearch.getKeyword(), mentionResponses, now);

        return new SearchResponse(
                savedSearch.getId(),
                savedSearch.getKeyword(),
                savedSearch.getResultsFound(),
                savedSearch.getSearchedAt(),
                mentionResponses
        );
    }

    public List<SearchHistory> findSearchHistory() {
        return searchHistoryRepository.findAll();
    }

    private List<MentionResponse> searchWithGNews(String keyword, OffsetDateTime collectedAt) {
        List<GNewsArticle> articles = gNewsClient.searchByKeyword(keyword);

        if (articles.isEmpty()) {
            return createFallbackMentions(keyword, collectedAt);
        }

        return articles.stream()
                .map(article -> saveMentionAndAnalysisFromArticle(keyword, collectedAt, article))
                .toList();
    }

    private MentionResponse saveMentionAndAnalysisFromArticle(String keyword, OffsetDateTime collectedAt, GNewsArticle article) {
        Mention mention = new Mention();
        mention.setKeyword(keyword);
        mention.setSource(resolveSourceName(article));
        mention.setAuthor(resolveAuthor(article));
        mention.setTitle(defaultIfBlank(article.title(), "Mention about " + keyword));
        mention.setContent(resolveContent(article, keyword));
        mention.setSourceUrl(article.url());
        mention.setPublishedAt(resolvePublishedAt(article.publishedAt(), collectedAt));
        mention.setCollectedAt(collectedAt);

        Mention savedMention = mentionRepository.save(mention);

        AnalysisResult analysisResult = new AnalysisResult();
        analysisResult.setMention(savedMention);
        analysisResult.setSentiment(inferSentiment(article));
        analysisResult.setReputationScore(inferScore(article));
        analysisResult.setTrending(Boolean.FALSE);
        analysisResult.setAnalyzedAt(collectedAt);
        AnalysisResult savedAnalysis = analysisResultRepository.save(analysisResult);

        return toMentionResponse(savedMention, savedAnalysis);
    }

    private List<MentionResponse> createFallbackMentions(String keyword, OffsetDateTime now) {
        List<FallbackMentionTemplate> templates = List.of(
                new FallbackMentionTemplate(
                        "mock-source",
                        "editorial-desk",
                        keyword + " ganha tracao apos nova campanha e registra forte repercussao",
                        "Modo demonstracao: mencao simulada com contexto positivo para ilustrar o comportamento do dashboard quando a API externa nao esta configurada.",
                        now.minusMinutes(135),
                        Sentiment.POSITIVE,
                        BigDecimal.valueOf(86.40),
                        Boolean.TRUE
                ),
                new FallbackMentionTemplate(
                        "mock-source",
                        "community-watch",
                        "Consumidores discutem " + keyword + " em tom neutro nas ultimas horas",
                        "Modo demonstracao: mencao simulada com repercussao neutra, util para preencher o painel mesmo sem GNews configurada.",
                        now.minusMinutes(88),
                        Sentiment.NEUTRAL,
                        DEFAULT_REPUTATION_SCORE,
                        Boolean.FALSE
                ),
                new FallbackMentionTemplate(
                        "mock-source",
                        "support-monitor",
                        keyword + " recebe reclamacoes apos falha operacional reportada por usuarios",
                        "Modo demonstracao: mencao simulada com viés negativo para habilitar alertas, score mais baixo e estados de risco no frontend.",
                        now.minusMinutes(54),
                        Sentiment.NEGATIVE,
                        BigDecimal.valueOf(41.20),
                        Boolean.TRUE
                ),
                new FallbackMentionTemplate(
                        "mock-source",
                        "market-digest",
                        "Analistas acompanham estabilidade de " + keyword + " enquanto o interesse segue alto",
                        "Modo demonstracao: mencao simulada com estabilidade de reputacao para compor a linha de base do monitoramento.",
                        now.minusMinutes(21),
                        Sentiment.NEUTRAL,
                        BigDecimal.valueOf(68.90),
                        Boolean.FALSE
                )
        );

        return templates.stream()
                .map(template -> saveFallbackMention(keyword, now, template))
                .toList();
    }

    private MentionResponse saveFallbackMention(String keyword, OffsetDateTime collectedAt, FallbackMentionTemplate template) {
        Mention mention = new Mention();
        mention.setKeyword(keyword);
        mention.setSource(template.source());
        mention.setAuthor(template.author());
        mention.setTitle(template.title());
        mention.setContent(template.content());
        mention.setSourceUrl("https://example.com/mentions/" + slugify(keyword) + "/" + slugify(template.author()));
        mention.setPublishedAt(template.publishedAt());
        mention.setCollectedAt(collectedAt);

        Mention savedMention = mentionRepository.save(mention);

        AnalysisResult analysisResult = new AnalysisResult();
        analysisResult.setMention(savedMention);
        analysisResult.setSentiment(template.sentiment());
        analysisResult.setReputationScore(template.reputationScore());
        analysisResult.setTrending(template.trending());
        analysisResult.setAnalyzedAt(collectedAt);
        AnalysisResult savedAnalysis = analysisResultRepository.save(analysisResult);

        return toMentionResponse(savedMention, savedAnalysis);
    }

    private void createAlertsForSearch(String keyword, List<MentionResponse> mentions, OffsetDateTime now) {
        long negativeMentions = mentions.stream()
                .filter(mention -> mention.sentiment() == Sentiment.NEGATIVE)
                .count();
        long trendingMentions = mentions.stream()
                .filter(mention -> Boolean.TRUE.equals(mention.trending()))
                .count();

        List<Alert> alertsToPersist = new ArrayList<>();

        if (negativeMentions > 0) {
            Alert alert = new Alert();
            alert.setTitle("Risco reputacional detectado para " + keyword);
            alert.setDescription("A busca encontrou " + negativeMentions
                    + " mencao(oes) negativa(s). Priorize leitura e resposta do time.");
            alert.setSeverity(AlertSeverity.HIGH);
            alert.setStatus(AlertStatus.OPEN);
            alert.setCreatedAt(now);
            alertsToPersist.add(alert);
        }

        if (trendingMentions > 0) {
            Alert alert = new Alert();
            alert.setTitle("Volume em destaque para " + keyword);
            alert.setDescription("A busca registrou " + trendingMentions
                    + " mencao(oes) marcadas como trending. Vale acompanhar a evolucao do tema.");
            alert.setSeverity(negativeMentions > 0 ? AlertSeverity.HIGH : AlertSeverity.MEDIUM);
            alert.setStatus(AlertStatus.OPEN);
            alert.setCreatedAt(now.plusSeconds(5));
            alertsToPersist.add(alert);
        }

        if (!alertsToPersist.isEmpty()) {
            alertRepository.saveAll(alertsToPersist);
        }
    }

    private MentionResponse toMentionResponse(Mention mention, AnalysisResult analysisResult) {
        return new MentionResponse(
                mention.getId(),
                mention.getKeyword(),
                mention.getSource(),
                mention.getAuthor(),
                mention.getTitle(),
                mention.getContent(),
                mention.getSourceUrl(),
                mention.getPublishedAt(),
                mention.getCollectedAt(),
                analysisResult.getSentiment(),
                analysisResult.getReputationScore(),
                analysisResult.getTrending()
        );
    }

    private Sentiment inferSentiment(GNewsArticle article) {
        String text = (defaultIfBlank(article.title(), "") + " " + defaultIfBlank(article.description(), "")).toLowerCase();

        if (text.contains("crisis") || text.contains("lawsuit") || text.contains("recall") || text.contains("drop")) {
            return Sentiment.NEGATIVE;
        }
        if (text.contains("growth") || text.contains("award") || text.contains("launch") || text.contains("record")) {
            return Sentiment.POSITIVE;
        }
        return Sentiment.NEUTRAL;
    }

    private BigDecimal inferScore(GNewsArticle article) {
        Sentiment sentiment = inferSentiment(article);

        if (sentiment == Sentiment.POSITIVE) {
            return BigDecimal.valueOf(85.00);
        }
        if (sentiment == Sentiment.NEGATIVE) {
            return BigDecimal.valueOf(40.00);
        }
        return DEFAULT_REPUTATION_SCORE;
    }

    private String resolveSourceName(GNewsArticle article) {
        if (article.source() != null && StringUtils.hasText(article.source().name())) {
            return article.source().name();
        }
        return "gnews";
    }

    private String resolveAuthor(GNewsArticle article) {
        return article.source() != null && StringUtils.hasText(article.source().name())
                ? article.source().name()
                : "unknown";
    }

    private String resolveContent(GNewsArticle article, String keyword) {
        if (StringUtils.hasText(article.content())) {
            return article.content();
        }
        if (StringUtils.hasText(article.description())) {
            return article.description();
        }
        return "External mention collected for keyword " + keyword + ".";
    }

    private OffsetDateTime resolvePublishedAt(String publishedAt, OffsetDateTime fallback) {
        if (!StringUtils.hasText(publishedAt)) {
            return fallback;
        }

        try {
            return Instant.parse(publishedAt).atOffset(ZoneOffset.UTC);
        } catch (Exception exception) {
            return fallback;
        }
    }

    private String defaultIfBlank(String value, String fallback) {
        return StringUtils.hasText(value) ? value : fallback;
    }

    private String slugify(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private record FallbackMentionTemplate(
            String source,
            String author,
            String title,
            String content,
            OffsetDateTime publishedAt,
            Sentiment sentiment,
            BigDecimal reputationScore,
            Boolean trending
    ) {
    }
}
