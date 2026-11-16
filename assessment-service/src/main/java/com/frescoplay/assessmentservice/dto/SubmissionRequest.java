
package com.frescoplay.assessmentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class SubmissionRequest {

    @NotNull
    @Min(0)
    private Integer score;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
