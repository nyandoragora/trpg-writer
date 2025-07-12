package com.example.trpg_writer.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.repository.SceneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SceneService {

    private final SceneRepository sceneRepository;

    public Page<Scene> findByScenarioOrderByCreatedAtAsc(Scenario scenario , Pageable pageable) {
        return sceneRepository.findByScenarioOrderByCreatedAtAsc(scenario , pageable);
    }

    public Optional<Scene> findById(Integer id) {
        return sceneRepository.findById(id);
    }
}
