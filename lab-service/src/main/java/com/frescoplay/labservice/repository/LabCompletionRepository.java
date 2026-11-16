package com.frescoplay.labservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.frescoplay.labservice.entity.LabCompletion;

public interface LabCompletionRepository
        extends JpaRepository<LabCompletion, Long> {

    List<LabCompletion> findByEmployeeId(Long employeeId);

    Optional<LabCompletion> findByEmployeeIdAndLabId(
            Long employeeId, Long labId);
}
