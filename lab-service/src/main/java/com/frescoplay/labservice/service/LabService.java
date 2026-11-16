
package com.frescoplay.labservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.frescoplay.labservice.dto.LabRequest;
import com.frescoplay.labservice.entity.Lab;
import com.frescoplay.labservice.repository.LabRepository;

@Service
public class LabService {

    private final LabRepository labRepository;

    public LabService(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

    public Lab getLabById(Long id) {
        return labRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("Lab not found"));
    }

    public Lab createLab(LabRequest request) {

        Lab lab = new Lab();

        lab.setTitle(request.getTitle());
        lab.setDescription(request.getDescription());
        lab.setCategory(request.getCategory());
        lab.setDifficulty(request.getDifficulty());
        lab.setInstructions(request.getInstructions());

        return labRepository.save(lab);
    }
}
