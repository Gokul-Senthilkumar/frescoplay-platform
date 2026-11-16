
package com.frescoplay.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.frescoplay.userservice.dto.LoginRequest;
import com.frescoplay.userservice.dto.LoginResponse;
import com.frescoplay.userservice.dto.RegisterRequest;
import com.frescoplay.userservice.dto.UserResponse;
import com.frescoplay.userservice.entity.Employee;
import com.frescoplay.userservice.service.EmployeeService;
import com.frescoplay.userservice.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserController(
            EmployeeService employeeService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.employeeService = employeeService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // Employee Registration
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        Employee employee = employeeService.registerEmployee(
                request.getFullName(),
                request.getEmail(),
                request.getPassword()
        );

        UserResponse response = new UserResponse(
                employee.getId(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getRole(),
                employee.getCreatedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Employee Login - JWT Generation
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getEmail().trim().toLowerCase(),
                        request.getPassword()
                    )
                );

        String token = jwtService.generateToken(authentication);

        LoginResponse response = new LoginResponse(
        token,
        "Bearer",
        jwtService.getExpirationSeconds()
);

        return ResponseEntity.ok(response);
    }

    // Get Authenticated Employee Profile
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication) {

        Employee employee = employeeService
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        UserResponse response = new UserResponse(
                employee.getId(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getRole(),
                employee.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }
}
