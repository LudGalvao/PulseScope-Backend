package com.pulsescope.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String keyword;

    @Column(nullable = false)
    private Integer resultsFound;

    @Column(nullable = false)
    private OffsetDateTime searchedAt;

    public SearchHistory() {
    }

    public SearchHistory(Long id, String keyword, Integer resultsFound, OffsetDateTime searchedAt) {
        this.id = id;
        this.keyword = keyword;
        this.resultsFound = resultsFound;
        this.searchedAt = searchedAt;
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

    public Integer getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(Integer resultsFound) {
        this.resultsFound = resultsFound;
    }

    public OffsetDateTime getSearchedAt() {
        return searchedAt;
    }

    public void setSearchedAt(OffsetDateTime searchedAt) {
        this.searchedAt = searchedAt;
    }
}
