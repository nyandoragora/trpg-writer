package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.SceneInfo;
import com.example.trpg_writer.entity.SceneInfoId;
import com.example.trpg_writer.repository.SceneInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SceneInfoService {

    private final SceneInfoRepository sceneInfoRepository;

    public Optional<SceneInfo> findById(SceneInfoId id) {
        return sceneInfoRepository.findById(id);
    }
}
