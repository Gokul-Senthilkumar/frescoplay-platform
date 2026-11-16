
package com.frescoplay.courseservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frescoplay.courseservice.dto.CourseRequest;
import com.frescoplay.courseservice.dto.CourseResponse;
import com.frescoplay.courseservice.dto.EnrollmentResponse;
import com.frescoplay.courseservice.service.CourseService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // List all courses
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {

        List<CourseResponse> courses = courseService.getAllCourses()
                .stream()
                .map(CourseResponse::new)
                .toList();

        return ResponseEntity.ok(courses);
    }

    // Create a course (ADMIN only)
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response = new CourseResponse(
                courseService.createCourse(request)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Enroll authenticated employee
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollmentResponse> enrollEmployee(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Jwt jwt) {

        Long employeeId = jwt.getClaim("employeeId");

        EnrollmentResponse response = new EnrollmentResponse(
                courseService.enrollEmployee(courseId, employeeId)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get enrollments for authenticated employee
@GetMapping("/my-enrollments")
public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(
        @AuthenticationPrincipal Jwt jwt) {

    Long employeeId = jwt.getClaim("employeeId");

    List<EnrollmentResponse> enrollments =
            courseService.getEmployeeEnrollments(employeeId)
                    .stream()
                    .map(EnrollmentResponse::new)
                    .toList();

    return ResponseEntity.ok(enrollments);
}
}
