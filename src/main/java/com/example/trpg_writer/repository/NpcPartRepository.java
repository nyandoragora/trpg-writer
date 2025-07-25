package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.NpcPart;
import com.example.trpg_writer.entity.NpcPartId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NpcPartRepository extends JpaRepository<NpcPart, NpcPartId> {
    List<NpcPart> findByNpc_ScenarioId(Integer scenarioId);
}
