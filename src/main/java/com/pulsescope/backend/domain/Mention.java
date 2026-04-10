package com.pulsescope.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "mentions")
public class Mention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String keyword;

    @Column(nullable = false, length = 150)
    private String source;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(length = 500)
    private String sourceUrl;

    @Column(nullable = false)
    private OffsetDateTime publishedAt;

    @Column(nullable = false)
    private OffsetDateTime collectedAt;

    public Mention() {
    }

    public Mention(Long id, String keyword, String source, String author, String title, String content, String sourceUrl, OffsetDateTime publishedAt, OffsetDateTime collectedAt) {
        this.id = id;
        this.keyword = keyword;
        this.source = source;
        this.author = author;
        this.title = title;
        this.content = content;
        this.sourceUrl = sourceUrl;
        this.publishedAt = publishedAt;
        this.collectedAt = collectedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public OffsetDateTime getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(OffsetDateTime collectedAt) {
        this.collectedAt = collectedAt;
    }
}
