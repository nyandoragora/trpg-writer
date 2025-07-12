package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.SceneNpc;
import com.example.trpg_writer.entity.SceneNpcId;
import com.example.trpg_writer.repository.SceneNpcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SceneNpcService {

    private final SceneNpcRepository sceneNpcRepository;

    public Optional<SceneNpc> findById(SceneNpcId id) {
        return sceneNpcRepository.findById(id);
    }

    public List<SceneNpc> findBySceneId(Integer sceneId) {
        return sceneNpcRepository.findBySceneId(sceneId);
    }
}
