
package com.frescoplay.labservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frescoplay.labservice.entity.Lab;
import com.frescoplay.labservice.entity.LabCompletion;
import com.frescoplay.labservice.repository.LabCompletionRepository;
import com.frescoplay.labservice.repository.LabRepository;

@Service
public class LabCompletionService {

    private final LabRepository labRepository;
    private final LabCompletionRepository completionRepository;

    public LabCompletionService(
            LabRepository labRepository,
            LabCompletionRepository completionRepository) {
        this.labRepository = labRepository;
        this.completionRepository = completionRepository;
    }

    @Transactional
    public LabCompletion completeLab(Long labId, Long employeeId) {

        Lab lab = labRepository.findById(labId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Lab not found"));

        if (completionRepository
                .findByEmployeeIdAndLabId(employeeId, labId)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Lab already completed");
        }

        LabCompletion completion = new LabCompletion();
        completion.setEmployeeId(employeeId);
        completion.setLabId(lab.getId());

        return completionRepository.save(completion);
    }

    public List<LabCompletion> getEmployeeCompletions(Long employeeId) {
        return completionRepository.findByEmployeeId(employeeId);
    }
}
