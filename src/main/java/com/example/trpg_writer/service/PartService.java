package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Part;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.form.PartForm;
import com.example.trpg_writer.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    public Optional<Part> findById(Integer id) {
        return partRepository.findById(id);
    }

    public List<Part> findByScenarioId(Integer scenarioId) {
        return partRepository.findByScenarioId(scenarioId);
    }

    @Transactional
    public Part create(PartForm partForm) {
        Part part = new Part();
        Scenario scenario = new Scenario();
        scenario.setId(partForm.getScenarioId());

        part.setScenario(scenario);
        part.setName(partForm.getName());
        part.setHit(partForm.getHit());
        part.setDamage(partForm.getDamage());
        part.setEvasion(partForm.getEvasion());
        part.setProtection(partForm.getProtection());
        part.setLifePoint(partForm.getLifePoint());
        part.setMagicPoint(partForm.getMagicPoint());

        return partRepository.save(part);
    }

    @Transactional
    public void delete(Integer id) {
        partRepository.deleteById(id);
    }
}
