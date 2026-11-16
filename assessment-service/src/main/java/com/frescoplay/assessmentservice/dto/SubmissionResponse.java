
package com.frescoplay.assessmentservice.dto;

import java.time.LocalDateTime;

import com.frescoplay.assessmentservice.entity.AssessmentSubmission;

public class SubmissionResponse {

    private Long id;
    private Long assessmentId;
    private Integer score;
    private Integer totalMarks;
    private String status;
    private LocalDateTime submittedAt;

    public SubmissionResponse(AssessmentSubmission submission) {
        this.id = submission.getId();
        this.assessmentId = submission.getAssessmentId();
        this.score = submission.getScore();
        this.totalMarks = submission.getTotalMarks();
        this.status = submission.getStatus();
        this.submittedAt = submission.getSubmittedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}
