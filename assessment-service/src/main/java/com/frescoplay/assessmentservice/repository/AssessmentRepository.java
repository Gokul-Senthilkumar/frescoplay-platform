package com.frescoplay.assessmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.frescoplay.assessmentservice.entity.Assessment;

public interface AssessmentRepository
        extends JpaRepository<Assessment, Long> {
}
