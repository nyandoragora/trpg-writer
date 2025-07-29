package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Skill;
import com.example.trpg_writer.form.SkillForm;
import com.example.trpg_writer.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public Optional<Skill> findById(Integer id) {
        return skillRepository.findById(id);
    }

    public List<Skill> findByScenarioId(Integer scenarioId) {
        return skillRepository.findByScenarioId(scenarioId);
    }

    @Transactional
    public Skill create(SkillForm skillForm) {
        Skill skill = new Skill();
        Scenario scenario = new Scenario();
        scenario.setId(skillForm.getScenarioId());

        skill.setScenario(scenario);
        skill.setName(skillForm.getName());
        skill.setContent(skillForm.getContent());

        return skillRepository.save(skill);
    }

    @Transactional
    public void delete(Integer id) {
        skillRepository.deleteById(id);
    }
}
