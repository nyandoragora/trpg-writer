package com.example.trpg_writer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.entity.SceneNpc;
import com.example.trpg_writer.repository.NpcRepository;
import com.example.trpg_writer.repository.SceneNpcRepository;
import com.example.trpg_writer.repository.SceneRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public final class SceneNpcService {

    private final SceneNpcRepository sceneNpcRepository;
    private final SceneRepository sceneRepository;
    private final NpcRepository npcRepository;

    public List<SceneNpc> findBySceneId(Integer sceneId) {
        return sceneNpcRepository.findBySceneId(sceneId);
    }

    @Transactional
    public void addNpcToScene(Integer sceneId, Integer npcId) {
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new EntityNotFoundException("Scene not found with id: " + sceneId));
        Npc npc = npcRepository.findById(npcId)
                .orElseThrow(() -> new EntityNotFoundException("Npc not found with id: " + npcId));

        SceneNpc sceneNpc = new SceneNpc();
        sceneNpc.setScene(scene);
        sceneNpc.setNpc(npc);
        
        sceneNpcRepository.save(sceneNpc);
    }

    @Transactional
    public void removeNpcFromScene(Long sceneNpcId) {
        if (!sceneNpcRepository.existsById(sceneNpcId)) {
            throw new EntityNotFoundException("SceneNpc not found with id: " + sceneNpcId);
        }
        sceneNpcRepository.deleteById(sceneNpcId);
    }
}
