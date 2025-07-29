package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Booty;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.form.BootyForm;
import com.example.trpg_writer.repository.BootyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BootyService {

    private final BootyRepository bootyRepository;

    public Optional<Booty> findById(Integer id) {
        return bootyRepository.findById(id);
    }

    public List<Booty> findByScenarioId(Integer scenarioId) {
        return bootyRepository.findByScenarioId(scenarioId);
    }

    @Transactional
    public Booty create(BootyForm bootyForm) {
        Booty booty = new Booty();
        Scenario scenario = new Scenario();
        scenario.setId(bootyForm.getScenarioId());

        booty.setScenario(scenario);
        booty.setDiceNum(bootyForm.getDiceNum());
        booty.setContent(bootyForm.getContent());

        return bootyRepository.save(booty);
    }

    @Transactional
    public void delete(Integer id) {
        bootyRepository.deleteById(id);
    }
}
