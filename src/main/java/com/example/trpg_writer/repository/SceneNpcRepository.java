package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.SceneNpc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SceneNpcRepository extends JpaRepository<SceneNpc, Long> {
    List<SceneNpc> findBySceneId(Integer sceneId);
    List<SceneNpc> findByScene_ScenarioId(Integer scenarioId);
}
