package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.Npc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NpcRepository extends JpaRepository<Npc, Integer> {
    List<Npc> findByScenarioId(Integer scenarioId);
}
