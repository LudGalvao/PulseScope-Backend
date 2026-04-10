package com.pulsescope.backend.repository;

import com.pulsescope.backend.domain.Alert;
import com.pulsescope.backend.domain.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    long countByStatus(AlertStatus status);
}
