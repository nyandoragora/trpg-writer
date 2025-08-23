package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.SceneInfo;
import com.example.trpg_writer.entity.SceneInfoId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SceneInfoRepository extends JpaRepository<SceneInfo, SceneInfoId> {
    List<SceneInfo> findBySceneId(Integer sceneId);
    List<SceneInfo> findByScene_ScenarioId(Integer scenarioId);
    void deleteByInfoId(Integer infoId);

    @Query("SELECT si.scene.title FROM SceneInfo si WHERE si.info.id = :infoId")
    List<String> findSceneTitlesByInfoId(@Param("infoId") Integer infoId);
}
