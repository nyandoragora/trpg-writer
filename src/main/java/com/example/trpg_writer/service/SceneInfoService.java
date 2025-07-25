package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.SceneInfo;
import com.example.trpg_writer.entity.SceneInfoId;
import com.example.trpg_writer.repository.SceneInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SceneInfoService {

    private final SceneInfoRepository sceneInfoRepository;

    public Optional<SceneInfo> findById(SceneInfoId id) {
        return sceneInfoRepository.findById(id);
    }

    public List<SceneInfo> findBySceneId(Integer sceneId) {
        return sceneInfoRepository.findBySceneId(sceneId);
    }

    public List<SceneInfo> findByScenarioId(Integer scenarioId) {
        return sceneInfoRepository.findByScene_ScenarioId(scenarioId);
    }
}
