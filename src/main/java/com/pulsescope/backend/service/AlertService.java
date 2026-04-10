package com.pulsescope.backend.service;

import com.pulsescope.backend.domain.Alert;
import com.pulsescope.backend.dto.AlertResponse;
import com.pulsescope.backend.repository.AlertRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<AlertResponse> findAll() {
        return alertRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AlertResponse updateStatus(Long id, com.pulsescope.backend.domain.AlertStatus status) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found"));
        alert.setStatus(status);
        return toResponse(alertRepository.save(alert));
    }

    public void delete(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found"));
        alertRepository.delete(alert);
    }

    private AlertResponse toResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getTitle(),
                alert.getDescription(),
                alert.getSeverity(),
                alert.getStatus(),
                alert.getCreatedAt()
        );
    }
}
