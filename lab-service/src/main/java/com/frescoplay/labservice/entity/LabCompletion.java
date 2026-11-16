
package com.frescoplay.labservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
    name = "lab_completions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_employee_lab",
            columnNames = {"employee_id", "lab_id"}
        )
    }
)
public class LabCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "lab_id", nullable = false)
    private Long labId;

    @Column(nullable = false, length = 30)
    private String status = "COMPLETED";

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    @PrePersist
    public void onCreate() {
        completedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getEmployeeId() { return employeeId; }
    public Long getLabId() { return labId; }
    public String getStatus() { return status; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }
}
