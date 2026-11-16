package com.frescoplay.courseservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    private String description;

    @NotBlank
    @Size(max = 100)
    private String category;

    @Min(1)
    private Integer durationHours;

    @NotBlank
    private String level;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getDurationHours() { return durationHours; }
    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
