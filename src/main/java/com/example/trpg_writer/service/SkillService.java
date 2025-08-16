package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Skill;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.SkillForm;
import com.example.trpg_writer.repository.ScenarioRepository;
import com.example.trpg_writer.repository.SkillRepository;
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
public final class SkillService {

    private final SkillRepository skillRepository;
    private final ScenarioRepository scenarioRepository;

    public Optional<Skill> findById(Integer id) {
        return skillRepository.findById(id);
    }

    public List<Skill> findByScenarioId(Integer scenarioId) {
        return skillRepository.findByScenarioId(scenarioId);
    }

    @Transactional
    public Skill create(SkillForm skillForm, User user) {
        Scenario scenario = scenarioRepository.findById(skillForm.getScenarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "シナリオが見つかりません。"));

        if (!scenario.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "シナリオの所有者が一致しません。");
        }

        Skill skill = new Skill();
        skill.setScenario(scenario);
        skill.setName(skillForm.getName());
        skill.setContent(skillForm.getContent());

        return skillRepository.save(skill);
    }

    @Transactional
    public void delete(Integer id, User user) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "特技が見つかりません。"));

        if (!skill.getScenario().getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "特技の所有者が一致しません。");
        }

        skillRepository.deleteById(id);
    }
}
