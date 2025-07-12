package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.repository.NpcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NpcService {

    private final NpcRepository npcRepository;

    public Optional<Npc> findById(Integer id) {
        return npcRepository.findById(id);
    }

    public List<Npc> findByScenarioId(Integer scenarioId) {
        return npcRepository.findByScenarioId(scenarioId);
    }
}
