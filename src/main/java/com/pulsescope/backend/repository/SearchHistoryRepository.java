package com.pulsescope.backend.repository;

import com.pulsescope.backend.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
}
