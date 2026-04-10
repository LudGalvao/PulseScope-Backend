package com.pulsescope.backend.repository;

import com.pulsescope.backend.domain.AnalysisResult;
import com.pulsescope.backend.domain.Sentiment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    long countBySentiment(Sentiment sentiment);

    Optional<AnalysisResult> findByMentionId(Long mentionId);
}
