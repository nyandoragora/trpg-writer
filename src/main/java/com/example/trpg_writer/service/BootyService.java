package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Booty;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.BootyForm;
import com.example.trpg_writer.repository.BootyRepository;
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
public class BootyService {

    private final BootyRepository bootyRepository;
    private final ScenarioRepository scenarioRepository;

    public Optional<Booty> findById(Integer id) {
        return bootyRepository.findById(id);
    }

    public List<Booty> findByScenarioId(Integer scenarioId) {
        return bootyRepository.findByScenarioId(scenarioId);
    }

    @Transactional
    public Booty create(BootyForm bootyForm, User user) {
        Scenario scenario = scenarioRepository.findById(bootyForm.getScenarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "シナリオが見つかりません。"));

        if (!scenario.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "シナリオの所有者が一致しません。");
        }

        Booty booty = new Booty();
        booty.setScenario(scenario);
        booty.setDiceNum(bootyForm.getDiceNum());
        booty.setContent(bootyForm.getContent());

        return bootyRepository.save(booty);
    }

    @Transactional
    public void delete(Integer id, User user) {
        Booty booty = bootyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "戦利品が見つかりません。"));

        if (!booty.getScenario().getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "戦利品の所有者が一致しません。");
        }

        bootyRepository.deleteById(id);
    }
}
