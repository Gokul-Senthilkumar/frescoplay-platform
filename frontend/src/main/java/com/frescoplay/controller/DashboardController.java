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
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${course.service.url}")
    private String courseServiceUrl;

    @Value("${assessment.service.url}")
    private String assessmentServiceUrl;

    @Value("${lab.service.url}")
    private String labServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            Map<String, Object> employee = restTemplate.exchange(
                    userServiceUrl + "/api/users/me",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            ).getBody();

            List<Map<String, Object>> courses = restTemplate.exchange(
                    courseServiceUrl + "/api/courses",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            List<Map<String, Object>> assessments = restTemplate.exchange(
                    assessmentServiceUrl + "/api/assessments",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            List<Map<String, Object>> labs = restTemplate.exchange(
                    labServiceUrl + "/api/labs",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            model.addAttribute("employee", employee);
            model.addAttribute("courses", courses);
            model.addAttribute("assessments", assessments);
            model.addAttribute("labs", labs);

            model.addAttribute("courseCount",
                    courses == null ? 0 : courses.size());

            model.addAttribute("assessmentCount",
                    assessments == null ? 0 : assessments.size());

            model.addAttribute("labCount",
                    labs == null ? 0 : labs.size());

            return "dashboard";

        } catch (Exception ex) {
            session.invalidate();
            return "redirect:/";
        }
    }
}
