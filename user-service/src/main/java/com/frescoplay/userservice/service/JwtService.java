
package com.frescoplay.userservice.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.frescoplay.userservice.entity.Employee;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final EmployeeService employeeService;
    private final long expirationMs;

    public JwtService(
            JwtEncoder jwtEncoder,
            EmployeeService employeeService,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {

        this.jwtEncoder = jwtEncoder;
        this.employeeService = employeeService;
        this.expirationMs = expirationMs;
    }

    public long getExpirationSeconds() {
        return expirationMs / 1000;
    }

    public String generateToken(Authentication authentication) {

        Instant now = Instant.now();

        Employee employee = employeeService.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException("Employee not found"));

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("fresco-play-user-service")
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationMs))
                .subject(authentication.getName())
                .claim("role", role)
                .claim("employeeId", employee.getId())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }
}
