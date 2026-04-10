package com.pulsescope.backend.repository;

import com.pulsescope.backend.domain.Mention;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentionRepository extends JpaRepository<Mention, Long> {

    List<Mention> findByKeywordIgnoreCaseOrderByCollectedAtDesc(String keyword);
}
