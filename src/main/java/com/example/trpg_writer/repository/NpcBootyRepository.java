package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.NpcBooty;
import com.example.trpg_writer.entity.NpcBootyId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NpcBootyRepository extends JpaRepository<NpcBooty, NpcBootyId> {
    List<NpcBooty> findByNpc_ScenarioId(Integer scenarioId);

    @Modifying
    @Query("DELETE FROM NpcBooty nb WHERE nb.npc.id = :npcId")
    void deleteByNpcId(@Param("npcId") Integer npcId);
}
