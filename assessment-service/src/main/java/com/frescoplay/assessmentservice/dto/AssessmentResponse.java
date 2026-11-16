
package com.frescoplay.assessmentservice.dto;

import java.time.LocalDateTime;

import com.frescoplay.assessmentservice.entity.Assessment;

public class AssessmentResponse {

    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private Integer totalQuestions;
    private Integer totalMarks;
    private LocalDateTime createdAt;

    public AssessmentResponse(Assessment assessment) {
        this.id = assessment.getId();
        this.title = assessment.getTitle();
        this.description = assessment.getDescription();
        this.courseId = assessment.getCourseId();
        this.totalQuestions = assessment.getTotalQuestions();
        this.totalMarks = assessment.getTotalMarks();
        this.createdAt = assessment.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
