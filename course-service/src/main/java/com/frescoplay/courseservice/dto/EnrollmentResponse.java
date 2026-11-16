package com.frescoplay.courseservice.dto;

import java.time.LocalDateTime;
import com.frescoplay.courseservice.entity.Enrollment;

public class EnrollmentResponse {

    private Long id;
    private Long courseId;
    private String courseTitle;
    private Integer progressPercentage;
    private String status;
    private LocalDateTime enrolledAt;

    public EnrollmentResponse(Enrollment enrollment) {
        this.id = enrollment.getId();
        this.courseId = enrollment.getCourse().getId();
        this.courseTitle = enrollment.getCourse().getTitle();
        this.progressPercentage = enrollment.getProgressPercentage();
        this.status = enrollment.getStatus();
        this.enrolledAt = enrollment.getEnrolledAt();
    }

    public Long getId() { return id; }
    public Long getCourseId() { return courseId; }
    public String getCourseTitle() { return courseTitle; }
    public Integer getProgressPercentage() { return progressPercentage; }
    public String getStatus() { return status; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
}
