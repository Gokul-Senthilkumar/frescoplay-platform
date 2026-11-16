package com.frescoplay.labservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.frescoplay.labservice.entity.Lab;

public interface LabRepository extends JpaRepository<Lab, Long> {
}
