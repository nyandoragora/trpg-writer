package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.NpcSkill;
import com.example.trpg_writer.entity.NpcSkillId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NpcSkillRepository extends JpaRepository<NpcSkill, NpcSkillId> {
    List<NpcSkill> findByNpc_ScenarioId(Integer scenarioId);
}
