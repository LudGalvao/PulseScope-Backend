package com.pulsescope.backend.service;

import com.pulsescope.backend.domain.AlertStatus;
import com.pulsescope.backend.domain.Sentiment;
import com.pulsescope.backend.dto.DashboardSummaryResponse;
import com.pulsescope.backend.repository.AlertRepository;
import com.pulsescope.backend.repository.AnalysisResultRepository;
import com.pulsescope.backend.repository.MentionRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final MentionRepository mentionRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final AlertRepository alertRepository;

    public DashboardService(
            MentionRepository mentionRepository,
            AnalysisResultRepository analysisResultRepository,
            AlertRepository alertRepository) {
        this.mentionRepository = mentionRepository;
        this.analysisResultRepository = analysisResultRepository;
        this.alertRepository = alertRepository;
    }

    public DashboardSummaryResponse getSummary() {
        return new DashboardSummaryResponse(
                mentionRepository.count(),
                analysisResultRepository.countBySentiment(Sentiment.POSITIVE),
                analysisResultRepository.countBySentiment(Sentiment.NEUTRAL),
                analysisResultRepository.countBySentiment(Sentiment.NEGATIVE),
                alertRepository.countByStatus(AlertStatus.OPEN)
        );
    }
}
