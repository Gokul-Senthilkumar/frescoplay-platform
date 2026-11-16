package com.frescoplay.assessmentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.frescoplay.assessmentservice.entity.AssessmentSubmission;

public interface AssessmentSubmissionRepository
        extends JpaRepository<AssessmentSubmission, Long> {

    List<AssessmentSubmission> findByEmployeeId(Long employeeId);

    Optional<AssessmentSubmission> findByEmployeeIdAndAssessmentId(
            Long employeeId, Long assessmentId);
}
