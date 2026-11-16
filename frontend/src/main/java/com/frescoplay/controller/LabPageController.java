
package com.frescoplay.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

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

@Controller
public class LabPageController {

    @Value("${lab.service.url}")
    private String labServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // Display labs and employee completion status
    @GetMapping("/labs")
    public String labs(HttpSession session, Model model) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            List<Map<String, Object>> labs = restTemplate.exchange(
                    labServiceUrl + "/api/labs",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            List<Map<String, Object>> completions = restTemplate.exchange(
                    labServiceUrl + "/api/labs/my-completions",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            Set<Long> completedLabIds = completions == null
                    ? Set.of()
                    : completions.stream()
                            .map(completion ->
                                    ((Number) completion.get("labId")).longValue())
                            .collect(Collectors.toSet());

            if (labs != null) {
                for (Map<String, Object> lab : labs) {

                    Long labId = ((Number) lab.get("id")).longValue();

                    lab.put("completed", completedLabIds.contains(labId));
                }
            }

            model.addAttribute("labs", labs);

            return "labs";

        } catch (Exception ex) {
            model.addAttribute(
                    "error",
                    "Unable to load technical labs. Please try again."
            );

            model.addAttribute("labs", List.of());

            return "labs";
        }
    }

    // Mark a lab as completed
    @PostMapping("/labs/{labId}/complete")
    public String completeLab(
            @PathVariable Long labId,
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
                    labServiceUrl + "/api/labs/" + labId + "/complete",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Lab marked as completed successfully!"
            );

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to complete lab. It may already be completed."
            );
        }

        return "redirect:/labs";
    }
}
