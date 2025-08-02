package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.SceneNpc;
import com.example.trpg_writer.repository.NpcRepository;
import com.example.trpg_writer.repository.SceneNpcRepository;
import com.example.trpg_writer.form.NpcForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException; 
import org.springframework.http.HttpStatus; 
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NpcService {

    private final NpcRepository npcRepository;
    private final ScenarioService scenarioService;
    private final SceneNpcRepository sceneNpcRepository;

    public Optional<Npc> findById(Integer id) {
        return npcRepository.findById(id);
    }

    public List<Npc> findByScenarioId(Integer scenarioId) {
        return npcRepository.findByScenarioId(scenarioId);
    }

    public List<Npc> findUniqueNpcsByScenarioId(Integer scenarioId) {
        List<SceneNpc> sceneNpcs = sceneNpcRepository.findByScene_ScenarioId(scenarioId);
        return sceneNpcs.stream()
                        .map(SceneNpc::getNpc)
                        .distinct()
                        .collect(Collectors.toList());
    }

    @Transactional
    public Npc create(NpcForm npcForm) {
        Npc npc = new Npc();
        Scenario scenario = scenarioService.findById(npcForm.getScenarioId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));

        npc.setScenario(scenario); 
        npc.setName(npcForm.getName());
        npc.setLevel(npcForm.getLevel());
        npc.setIntelligence(npcForm.getIntelligence());
        npc.setPerception(npcForm.getPerception());
        npc.setPosition(npcForm.getPosition());
        npc.setImpurity(npcForm.getImpurity());
        npc.setLanguage(npcForm.getLanguage());
        npc.setHabitat(npcForm.getHabitat());
        npc.setPopularity(npcForm.getPopularity());
        npc.setWeakness(npcForm.getWeakness());
        npc.setPreemptive(npcForm.getPreemptive());
        npc.setMovement(npcForm.getMovement());
        npc.setLifeResist(npcForm.getLifeResist());
        npc.setMindResist(npcForm.getMindResist());
        npc.setDescription(npcForm.getDescription());

        return npcRepository.save(npc);
    }

    @Transactional
    public void delete(Integer id) {
        npcRepository.deleteById(id);
    }
}
