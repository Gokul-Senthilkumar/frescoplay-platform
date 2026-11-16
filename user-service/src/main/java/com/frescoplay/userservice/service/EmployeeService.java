
package com.frescoplay.userservice.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frescoplay.userservice.entity.Employee;
import com.frescoplay.userservice.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Employee registerEmployee(
            String fullName, String email, String rawPassword) {

        String normalizedEmail = email.trim().toLowerCase();

        if (employeeRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        Employee employee = new Employee();

        employee.setFullName(fullName.trim());
        employee.setEmail(normalizedEmail);
        employee.setPassword(passwordEncoder.encode(rawPassword));
        employee.setRole("EMPLOYEE");

        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(
                email.trim().toLowerCase());
    }
}
