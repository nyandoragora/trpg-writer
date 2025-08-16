package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Part;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.PartForm;
import com.example.trpg_writer.repository.PartRepository;
import com.example.trpg_writer.repository.ScenarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public final class PartService {

    private final PartRepository partRepository;
    private final ScenarioRepository scenarioRepository;

    public Optional<Part> findById(Integer id) {
        return partRepository.findById(id);
    }

    public List<Part> findByScenarioId(Integer scenarioId) {
        return partRepository.findByScenarioId(scenarioId);
    }

    @Transactional
    public Part create(PartForm partForm, User user) {
        Scenario scenario = scenarioRepository.findById(partForm.getScenarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "シナリオが見つかりません。"));

        if (!scenario.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "シナリオの所有者が一致しません。");
        }

        Part part = new Part();
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
    public void delete(Integer id, User user) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "部位が見つかりません。"));

        if (!part.getScenario().getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "部位の所有者が一致しません。");
        }

        partRepository.deleteById(id);
    }
}
