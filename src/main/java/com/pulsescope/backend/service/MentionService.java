package com.pulsescope.backend.service;

import com.pulsescope.backend.domain.AnalysisResult;
import com.pulsescope.backend.domain.Mention;
import com.pulsescope.backend.dto.MentionResponse;
import com.pulsescope.backend.repository.AnalysisResultRepository;
import com.pulsescope.backend.repository.MentionRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MentionService {

    private final MentionRepository mentionRepository;
    private final AnalysisResultRepository analysisResultRepository;

    public MentionService(MentionRepository mentionRepository, AnalysisResultRepository analysisResultRepository) {
        this.mentionRepository = mentionRepository;
        this.analysisResultRepository = analysisResultRepository;
    }

    public List<MentionResponse> findAll() {
        return mentionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MentionResponse findById(Long id) {
        Mention mention = mentionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mention not found"));
        return toResponse(mention);
    }

    private MentionResponse toResponse(Mention mention) {
        AnalysisResult analysisResult = analysisResultRepository.findByMentionId(mention.getId())
                .orElse(null);

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
                analysisResult != null ? analysisResult.getSentiment() : null,
                analysisResult != null ? analysisResult.getReputationScore() : null,
                analysisResult != null ? analysisResult.getTrending() : null
        );
    }
}
