package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.Part;
import com.example.trpg_writer.entity.NpcPart;
import com.example.trpg_writer.entity.NpcPartId;
import com.example.trpg_writer.repository.NpcPartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class NpcPartService {

    private final NpcPartRepository npcPartRepository;

    public Optional<NpcPart> findById(NpcPartId id) {
        return npcPartRepository.findById(id);
    }

    public List<NpcPart> findByNpcScenarioId(Integer scenarioId) {
        return npcPartRepository.findByNpc_ScenarioId(scenarioId);
    }

    @Transactional
    public void create(Npc npc, Part part) {
        NpcPart npcPart = new NpcPart();
        npcPart.setNpc(npc);
        npcPart.setPart(part);
        npcPartRepository.save(npcPart);
    }

    @Transactional
    public void deleteByNpcId(Integer npcId) {
        npcPartRepository.deleteByNpcId(npcId);
    }

    @Transactional
    public void deleteByNpcIdAndPartId(Integer npcId, Integer partId) {
        npcPartRepository.deleteByNpcIdAndPartId(npcId, partId);
    }
}
