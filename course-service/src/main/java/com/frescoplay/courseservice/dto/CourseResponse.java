package com.frescoplay.courseservice.dto;

import java.time.LocalDateTime;
import com.frescoplay.courseservice.entity.Course;

public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private Integer durationHours;
    private String level;
    private LocalDateTime createdAt;

    public CourseResponse(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.category = course.getCategory();
        this.durationHours = course.getDurationHours();
        this.level = course.getLevel();
        this.createdAt = course.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Integer getDurationHours() { return durationHours; }
    public String getLevel() { return level; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
