package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.Skill;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    List<Skill> findByScenarioId(Integer scenarioId);
}
