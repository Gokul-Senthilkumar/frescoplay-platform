
package com.frescoplay.assessmentservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.frescoplay.assessmentservice.dto.*;
import com.frescoplay.assessmentservice.service.AssessmentService;
import com.frescoplay.assessmentservice.service.AssessmentSubmissionService;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final AssessmentSubmissionService submissionService;

    public AssessmentController(
            AssessmentService assessmentService,
            AssessmentSubmissionService submissionService) {
        this.assessmentService = assessmentService;
        this.submissionService = submissionService;
    }

    @GetMapping
    public ResponseEntity<List<AssessmentResponse>> getAllAssessments() {
        List<AssessmentResponse> assessments =
                assessmentService.getAllAssessments()
                        .stream()
                        .map(AssessmentResponse::new)
                        .toList();

        return ResponseEntity.ok(assessments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponse> getAssessment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new AssessmentResponse(
                        assessmentService.getAssessmentById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<AssessmentResponse> createAssessment(
            @Valid @RequestBody AssessmentRequest request) {

        AssessmentResponse response = new AssessmentResponse(
                assessmentService.createAssessment(request)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmissionResponse> submitAssessment(
            @PathVariable Long id,
            @Valid @RequestBody SubmissionRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        Long employeeId = jwt.getClaim("employeeId");

        SubmissionResponse response = new SubmissionResponse(
                submissionService.submitAssessment(
                        id, employeeId, request.getScore()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-results")
    public ResponseEntity<List<SubmissionResponse>> getMyResults(
            @AuthenticationPrincipal Jwt jwt) {

        Long employeeId = jwt.getClaim("employeeId");

        List<SubmissionResponse> results =
                submissionService.getEmployeeResults(employeeId)
                        .stream()
                        .map(SubmissionResponse::new)
                        .toList();

        return ResponseEntity.ok(results);
    }
}
