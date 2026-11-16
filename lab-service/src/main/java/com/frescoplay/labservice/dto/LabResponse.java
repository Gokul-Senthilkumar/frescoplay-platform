
package com.frescoplay.labservice.dto;

import java.time.LocalDateTime;

import com.frescoplay.labservice.entity.Lab;

public class LabResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String difficulty;
    private String instructions;
    private LocalDateTime createdAt;

    public LabResponse(Lab lab) {
        this.id = lab.getId();
        this.title = lab.getTitle();
        this.description = lab.getDescription();
        this.category = lab.getCategory();
        this.difficulty = lab.getDifficulty();
        this.instructions = lab.getInstructions();
        this.createdAt = lab.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public String getInstructions() { return instructions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
