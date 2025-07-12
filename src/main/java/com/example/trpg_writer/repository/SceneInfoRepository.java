package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.SceneInfo;
import com.example.trpg_writer.entity.SceneInfoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SceneInfoRepository extends JpaRepository<SceneInfo, SceneInfoId> {
    List<SceneInfo> findBySceneId(Integer sceneId);
}
