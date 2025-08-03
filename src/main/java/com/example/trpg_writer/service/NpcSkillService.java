package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.Skill;
import com.example.trpg_writer.entity.NpcSkill;
import com.example.trpg_writer.entity.NpcSkillId;
import com.example.trpg_writer.repository.NpcSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NpcSkillService {

    private final NpcSkillRepository npcSkillRepository;

    public Optional<NpcSkill> findById(NpcSkillId id) {
        return npcSkillRepository.findById(id);
    }

    public List<NpcSkill> findByNpcScenarioId(Integer scenarioId) {
        return npcSkillRepository.findByNpc_ScenarioId(scenarioId);
    }

    @Transactional
    public void create(Npc npc, Skill skill) {
        NpcSkill npcSkill = new NpcSkill();
        npcSkill.setNpc(npc);
        npcSkill.setSkill(skill);
        npcSkillRepository.save(npcSkill);
    }

    @Transactional
    public void deleteByNpcId(Integer npcId) {
        npcSkillRepository.deleteByNpcId(npcId);
    }
}
