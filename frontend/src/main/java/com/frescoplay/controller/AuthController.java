
package com.frescoplay.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Value("${user.service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("accessToken") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("accessToken") != null) {
            return "redirect:/dashboard";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        try {
            Map<String, String> request = Map.of(
                    "fullName", fullName.trim(),
                    "email", email.trim(),
                    "password", password
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    userServiceUrl + "/api/users/register",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute(
                        "success",
                        "Registration successful! Please sign in."
                );
                return "redirect:/";
            }

            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";

        } catch (HttpClientErrorException.Conflict ex) {
            model.addAttribute("error", "This email is already registered.");
            return "register";

        } catch (HttpClientErrorException ex) {
            model.addAttribute(
                    "error",
                    "Please check your details. Password must be at least 8 characters."
            );
            return "register";

        } catch (Exception ex) {
            model.addAttribute(
                    "error",
                    "Unable to register right now. Please try again."
            );
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            Map<String, String> request = Map.of(
                    "email", email,
                    "password", password
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    userServiceUrl + "/api/users/login",
                    request,
                    Map.class
            );

            Map<String, Object> result = response.getBody();

            if (result == null || result.get("accessToken") == null) {
                model.addAttribute("error", "Login failed. Please try again.");
                return "login";
            }

            session.setAttribute("accessToken", result.get("accessToken"));

            return "redirect:/dashboard";

        } catch (Exception ex) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
