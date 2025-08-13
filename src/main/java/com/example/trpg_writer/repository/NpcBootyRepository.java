package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.NpcBooty;
import com.example.trpg_writer.entity.NpcBootyId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NpcBootyRepository extends JpaRepository<NpcBooty, NpcBootyId> {
    List<NpcBooty> findByNpc_ScenarioId(Integer scenarioId);

    @Transactional
    void deleteByNpcId(Integer npcId);

    @Transactional
    void deleteByNpcIdAndBootyId(Integer npcId, Integer bootyId);
}