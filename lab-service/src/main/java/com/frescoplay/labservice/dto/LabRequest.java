
package com.frescoplay.labservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LabRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    private String description;

    @NotBlank
    @Size(max = 100)
    private String category;

    @NotBlank
    private String difficulty;

    @NotBlank
    private String instructions;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public String getInstructions() { return instructions; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
