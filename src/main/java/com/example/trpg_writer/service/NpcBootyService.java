package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.NpcBooty;
import com.example.trpg_writer.entity.NpcBootyId;
import com.example.trpg_writer.repository.NpcBootyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NpcBootyService {

    private final NpcBootyRepository npcBootyRepository;

    public Optional<NpcBooty> findById(NpcBootyId id) {
        return npcBootyRepository.findById(id);
    }

    public List<NpcBooty> findByNpcScenarioId(Integer scenarioId) {
        return npcBootyRepository.findByNpc_ScenarioId(scenarioId);
    }
}
