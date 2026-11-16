
package com.frescoplay.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class AssessmentPageController {

    @Value("${assessment.service.url}")
    private String assessmentServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/assessments")
    public String assessments(HttpSession session, Model model) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            List<Map<String, Object>> assessments = restTemplate.exchange(
                    assessmentServiceUrl + "/api/assessments",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            List<Map<String, Object>> results = restTemplate.exchange(
                    assessmentServiceUrl + "/api/assessments/my-results",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            Map<Long, Map<String, Object>> submittedAssessments =
                    results == null
                    ? Map.of()
                    : results.stream().collect(Collectors.toMap(
                            result -> ((Number) result.get("assessmentId")).longValue(),
                            Function.identity()
                    ));

            if (assessments != null) {
                for (Map<String, Object> assessment : assessments) {

                    Long assessmentId =
                            ((Number) assessment.get("id")).longValue();

                    Map<String, Object> result =
                            submittedAssessments.get(assessmentId);

                    assessment.put("submitted", result != null);

                    if (result != null) {
                        assessment.put("submittedScore", result.get("score"));
                        assessment.put("submittedTotalMarks",
                                result.get("totalMarks"));
                    }
                }
            }

            model.addAttribute("assessments", assessments);
            model.addAttribute("results", results);

            return "assessments";

        } catch (Exception ex) {
            model.addAttribute(
                    "error",
                    "Unable to load assessments. Please try again."
            );

            model.addAttribute("assessments", List.of());
            model.addAttribute("results", List.of());

            return "assessments";
        }
    }

    @PostMapping("/assessments/{assessmentId}/submit")
    public String submitAssessment(
            @PathVariable Long assessmentId,
            @RequestParam Integer score,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Integer> request = Map.of("score", score);

            HttpEntity<Map<String, Integer>> entity =
                    new HttpEntity<>(request, headers);

            restTemplate.exchange(
                    assessmentServiceUrl + "/api/assessments/"
                            + assessmentId + "/submit",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Assessment submitted successfully!"
            );

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to submit assessment. It may already be submitted or the score is invalid."
            );
        }

        return "redirect:/assessments";
    }
}
