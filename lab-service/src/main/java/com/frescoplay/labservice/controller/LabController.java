
package com.frescoplay.labservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.frescoplay.labservice.dto.*;
import com.frescoplay.labservice.service.LabService;
import com.frescoplay.labservice.service.LabCompletionService;

@RestController
@RequestMapping("/api/labs")
public class LabController {

    private final LabService labService;
    private final LabCompletionService completionService;

    public LabController(
            LabService labService,
            LabCompletionService completionService) {
        this.labService = labService;
        this.completionService = completionService;
    }

    @GetMapping
    public ResponseEntity<List<LabResponse>> getAllLabs() {
        List<LabResponse> labs = labService.getAllLabs()
                .stream()
                .map(LabResponse::new)
                .toList();

        return ResponseEntity.ok(labs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabResponse> getLab(@PathVariable Long id) {
        return ResponseEntity.ok(
                new LabResponse(labService.getLabById(id))
        );
    }

    @PostMapping
    public ResponseEntity<LabResponse> createLab(
            @Valid @RequestBody LabRequest request) {

        LabResponse response = new LabResponse(
                labService.createLab(request)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeLab(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Long employeeId = jwt.getClaim("employeeId");

        return ResponseEntity.status(HttpStatus.CREATED).body(
                completionService.completeLab(id, employeeId)
        );
    }

    @GetMapping("/my-completions")
    public ResponseEntity<?> getMyCompletions(
            @AuthenticationPrincipal Jwt jwt) {

        Long employeeId = jwt.getClaim("employeeId");

        return ResponseEntity.ok(
                completionService.getEmployeeCompletions(employeeId)
        );
    }
}
