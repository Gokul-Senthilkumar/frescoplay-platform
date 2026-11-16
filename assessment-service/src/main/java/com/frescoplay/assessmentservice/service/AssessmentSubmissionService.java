
package com.frescoplay.assessmentservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frescoplay.assessmentservice.entity.Assessment;
import com.frescoplay.assessmentservice.entity.AssessmentSubmission;
import com.frescoplay.assessmentservice.repository.AssessmentRepository;
import com.frescoplay.assessmentservice.repository.AssessmentSubmissionRepository;

@Service
public class AssessmentSubmissionService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentSubmissionRepository submissionRepository;

    public AssessmentSubmissionService(
            AssessmentRepository assessmentRepository,
            AssessmentSubmissionRepository submissionRepository) {
        this.assessmentRepository = assessmentRepository;
        this.submissionRepository = submissionRepository;
    }

    @Transactional
    public AssessmentSubmission submitAssessment(
            Long assessmentId, Long employeeId, Integer score) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Assessment not found"));

        if (score < 0 || score > assessment.getTotalMarks()) {
            throw new IllegalArgumentException("Invalid assessment score");
        }

        if (submissionRepository
                .findByEmployeeIdAndAssessmentId(employeeId, assessmentId)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Assessment already submitted");
        }

        AssessmentSubmission submission = new AssessmentSubmission();
        submission.setEmployeeId(employeeId);
        submission.setAssessmentId(assessmentId);
        submission.setScore(score);
        submission.setTotalMarks(assessment.getTotalMarks());

        return submissionRepository.save(submission);
    }

    public List<AssessmentSubmission> getEmployeeResults(Long employeeId) {
        return submissionRepository.findByEmployeeId(employeeId);
    }
}
