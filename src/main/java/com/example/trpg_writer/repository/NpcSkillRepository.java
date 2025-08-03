package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.NpcSkill;
import com.example.trpg_writer.entity.NpcSkillId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NpcSkillRepository extends JpaRepository<NpcSkill, NpcSkillId> {
    List<NpcSkill> findByNpc_ScenarioId(Integer scenarioId);

    @Modifying
    @Query("DELETE FROM NpcSkill ns WHERE ns.npc.id = :npcId")
    void deleteByNpcId(@Param("npcId") Integer npcId);
}
