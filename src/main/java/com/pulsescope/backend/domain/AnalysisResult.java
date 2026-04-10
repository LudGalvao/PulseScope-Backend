package com.pulsescope.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "analysis_results")
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mention_id", nullable = false, unique = true)
    private Mention mention;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sentiment sentiment;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal reputationScore;

    @Column(nullable = false)
    private Boolean trending;

    @Column(nullable = false)
    private OffsetDateTime analyzedAt;

    public AnalysisResult() {
    }

    public AnalysisResult(Long id, Mention mention, Sentiment sentiment, BigDecimal reputationScore, Boolean trending, OffsetDateTime analyzedAt) {
        this.id = id;
        this.mention = mention;
        this.sentiment = sentiment;
        this.reputationScore = reputationScore;
        this.trending = trending;
        this.analyzedAt = analyzedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mention getMention() {
        return mention;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public BigDecimal getReputationScore() {
        return reputationScore;
    }

    public void setReputationScore(BigDecimal reputationScore) {
        this.reputationScore = reputationScore;
    }

    public Boolean getTrending() {
        return trending;
    }

    public void setTrending(Boolean trending) {
        this.trending = trending;
    }

    public OffsetDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(OffsetDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }
}
