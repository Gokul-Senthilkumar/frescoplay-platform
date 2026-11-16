
package com.frescoplay.assessmentservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.frescoplay.assessmentservice.dto.AssessmentRequest;
import com.frescoplay.assessmentservice.entity.Assessment;
import com.frescoplay.assessmentservice.repository.AssessmentRepository;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Assessment createAssessment(AssessmentRequest request) {

        Assessment assessment = new Assessment();

        assessment.setTitle(request.getTitle());
        assessment.setDescription(request.getDescription());
        assessment.setCourseId(request.getCourseId());
        assessment.setTotalQuestions(request.getTotalQuestions());
        assessment.setTotalMarks(request.getTotalMarks());

        return assessmentRepository.save(assessment);
    }

    public Assessment getAssessmentById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("Assessment not found"));
    }
}
