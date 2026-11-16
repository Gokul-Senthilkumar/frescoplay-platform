
package com.frescoplay.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class CoursePageController {

    @Value("${course.service.url}")
    private String courseServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/courses")
    public String courses(HttpSession session, Model model) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // Fetch all available courses
            List<Map<String, Object>> courses = restTemplate.exchange(
                    courseServiceUrl + "/api/courses",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            // Fetch employee-specific enrollments
            List<Map<String, Object>> enrollments = restTemplate.exchange(
                    courseServiceUrl + "/api/courses/my-enrollments",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            // Normalize IDs and mark enrollment status
            List<Long> enrolledCourseIds = enrollments == null
                    ? List.of()
                    : enrollments.stream()
                            .map(enrollment ->
                                    ((Number) enrollment.get("courseId")).longValue())
                            .toList();

            if (courses != null) {
                for (Map<String, Object> course : courses) {

                    Long courseId =
                            ((Number) course.get("id")).longValue();

                    course.put(
                            "enrolled",
                            enrolledCourseIds.contains(courseId)
                    );
                }
            }

            model.addAttribute("courses", courses);

            return "courses";

        } catch (Exception ex) {
            model.addAttribute(
                    "error",
                    "Unable to load courses. Please try again."
            );

            model.addAttribute("courses", List.of());

            return "courses";
        }
    }

    @PostMapping("/courses/{courseId}/enroll")
    public String enrollCourse(
            @PathVariable Long courseId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                    courseServiceUrl + "/api/courses/" + courseId + "/enroll",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Successfully enrolled in the course!"
            );

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to enroll. You may already be enrolled."
            );
        }

        return "redirect:/courses";
    }
}
