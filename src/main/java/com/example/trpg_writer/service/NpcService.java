package com.example.trpg_writer.service;

import com.example.trpg_writer.dto.NpcDetailDto;
import com.example.trpg_writer.entity.Booty;
import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.NpcPart;
import com.example.trpg_writer.entity.NpcSkill;
import com.example.trpg_writer.entity.NpcBooty;
import com.example.trpg_writer.entity.Part;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.SceneNpc;
import com.example.trpg_writer.entity.Skill;
import com.example.trpg_writer.repository.BootyRepository;
import com.example.trpg_writer.repository.NpcRepository;
import com.example.trpg_writer.repository.PartRepository;
import com.example.trpg_writer.repository.SceneNpcRepository;
import com.example.trpg_writer.repository.SkillRepository;
import com.example.trpg_writer.form.NpcForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException; 
import org.springframework.http.HttpStatus; 
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public final class NpcService {

    private final NpcRepository npcRepository;
    private final ScenarioService scenarioService;
    private final SceneNpcRepository sceneNpcRepository;
    private final PartRepository partRepository;
    private final SkillRepository skillRepository;
    private final BootyRepository bootyRepository;

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
        
        if (npcForm.getPartIds() != null) {
            npcForm.getPartIds().forEach(partId -> {
                partRepository.findById(partId).ifPresent(part -> {
                    NpcPart npcPart = new NpcPart();
                    npcPart.setNpc(npc);
                    npcPart.setPart(part);
                    npc.getParts().add(npcPart);
                });
            });
        }
        if (npcForm.getSkillIds() != null) {
            npcForm.getSkillIds().forEach(skillId -> {
                skillRepository.findById(skillId).ifPresent(skill -> {
                    NpcSkill npcSkill = new NpcSkill();
                    npcSkill.setNpc(npc);
                    npcSkill.setSkill(skill);
                    npc.getSkills().add(npcSkill);
                });
            });
        }
        if (npcForm.getBootyIds() != null) {
            npcForm.getBootyIds().forEach(bootyId -> {
                bootyRepository.findById(bootyId).ifPresent(booty -> {
                    NpcBooty npcBooty = new NpcBooty();
                    npcBooty.setNpc(npc);
                    npcBooty.setBooty(booty);
                    npc.getBootys().add(npcBooty);
                });
            });
        }

        return npcRepository.save(npc);
    }

    @Transactional
    public void delete(Integer id) {
        npcRepository.deleteById(id);
    }

    @Transactional
    public Npc update(NpcForm npcForm) {
        Npc npc = npcRepository.findById(npcForm.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NPC not found"));
        
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

        updateParts(npc, npcForm.getPartIds());
        updateSkills(npc, npcForm.getSkillIds());
        updateBootys(npc, npcForm.getBootyIds());

        return npcRepository.save(npc);
    }

    private void updateParts(Npc npc, List<Integer> newPartIds) {
        final List<Integer> processedNewPartIds = (newPartIds == null) ? new ArrayList<>() : newPartIds;
        
        npc.getParts().removeIf(npcPart -> !processedNewPartIds.contains(npcPart.getPart().getId()));

        List<Integer> currentPartIds = npc.getParts().stream()
                                           .map(npcPart -> npcPart.getPart().getId())
                                           .collect(Collectors.toList());
        List<Integer> idsToAdd = new ArrayList<>(processedNewPartIds);
        idsToAdd.removeAll(currentPartIds);

        idsToAdd.forEach(partId -> {
            partRepository.findById(partId).ifPresent(part -> {
                NpcPart newNpcPart = new NpcPart();
                newNpcPart.setNpc(npc);
                newNpcPart.setPart(part);
                npc.getParts().add(newNpcPart);
            });
        });
    }

    private void updateSkills(Npc npc, List<Integer> newSkillIds) {
        final List<Integer> processedNewSkillIds = (newSkillIds == null) ? new ArrayList<>() : newSkillIds;

        npc.getSkills().removeIf(npcSkill -> !processedNewSkillIds.contains(npcSkill.getSkill().getId()));

        List<Integer> currentSkillIds = npc.getSkills().stream()
                                            .map(npcSkill -> npcSkill.getSkill().getId())
                                            .collect(Collectors.toList());
        List<Integer> idsToAdd = new ArrayList<>(processedNewSkillIds);
        idsToAdd.removeAll(currentSkillIds);

        idsToAdd.forEach(skillId -> {
            skillRepository.findById(skillId).ifPresent(skill -> {
                NpcSkill newNpcSkill = new NpcSkill();
                newNpcSkill.setNpc(npc);
                newNpcSkill.setSkill(skill);
                npc.getSkills().add(newNpcSkill);
            });
        });
    }

    private void updateBootys(Npc npc, List<Integer> newBootyIds) {
        final List<Integer> processedNewBootyIds = (newBootyIds == null) ? new ArrayList<>() : newBootyIds;

        npc.getBootys().removeIf(npcBooty -> !processedNewBootyIds.contains(npcBooty.getBooty().getId()));

        List<Integer> currentBootyIds = npc.getBootys().stream()
                                            .map(npcBooty -> npcBooty.getBooty().getId())
                                            .collect(Collectors.toList());
        List<Integer> idsToAdd = new ArrayList<>(processedNewBootyIds);
        idsToAdd.removeAll(currentBootyIds);

        idsToAdd.forEach(bootyId -> {
            bootyRepository.findById(bootyId).ifPresent(booty -> {
                NpcBooty newNpcBooty = new NpcBooty();
                newNpcBooty.setNpc(npc);
                newNpcBooty.setBooty(booty);
                npc.getBootys().add(newNpcBooty);
            });
        });
    }

    public NpcForm convertEntityToForm(Npc npc) {
        NpcForm npcForm = new NpcForm();
        npcForm.setId(npc.getId());
        npcForm.setScenarioId(npc.getScenario().getId());
        npcForm.setName(npc.getName());
        npcForm.setLevel(npc.getLevel());
        npcForm.setIntelligence(npc.getIntelligence());
        npcForm.setPerception(npc.getPerception());
        npcForm.setPosition(npc.getPosition());
        npcForm.setImpurity(npc.getImpurity());
        npcForm.setLanguage(npc.getLanguage());
        npcForm.setHabitat(npc.getHabitat());
        npcForm.setPopularity(npc.getPopularity());
        npcForm.setWeakness(npc.getWeakness());
        npcForm.setPreemptive(npc.getPreemptive());
        npcForm.setMovement(npc.getMovement());
        npcForm.setLifeResist(npc.getLifeResist());
        npcForm.setMindResist(npc.getMindResist());
        npcForm.setDescription(npc.getDescription());
        
        npcForm.setPartIds(npc.getParts().stream().map(np -> np.getPart().getId()).collect(Collectors.toList()));
        npcForm.setSkillIds(npc.getSkills().stream().map(ns -> ns.getSkill().getId()).collect(Collectors.toList()));
        npcForm.setBootyIds(npc.getBootys().stream().map(nb -> nb.getBooty().getId()).collect(Collectors.toList()));

        return npcForm;
    }

    public NpcDetailDto convertToDetailDto(Npc npc) {
        NpcDetailDto dto = new NpcDetailDto();
        dto.setId(npc.getId());
        dto.setName(npc.getName());
        dto.setDescription(npc.getDescription());
        dto.setLevel(npc.getLevel());
        dto.setIntelligence(npc.getIntelligence());
        dto.setPerception(npc.getPerception());
        dto.setPosition(npc.getPosition());
        dto.setLanguage(npc.getLanguage());
        dto.setPopularity(npc.getPopularity());
        dto.setWeakness(npc.getWeakness());
        dto.setPreemptive(npc.getPreemptive());
        dto.setMovement(npc.getMovement());
        dto.setLifeResist(npc.getLifeResist());
        dto.setMindResist(npc.getMindResist());
        dto.setImpurity(npc.getImpurity());
        dto.setHabitat(npc.getHabitat());

        dto.setParts(npc.getParts().stream().map(npcPart -> {
            NpcDetailDto.PartDto partDto = new NpcDetailDto.PartDto();
            partDto.setName(npcPart.getPart().getName());
            partDto.setHit(npcPart.getPart().getHit());
            partDto.setDamage(npcPart.getPart().getDamage());
            partDto.setEvasion(npcPart.getPart().getEvasion());
            partDto.setDefense(npcPart.getPart().getProtection());
            partDto.setHitPoint(npcPart.getPart().getLifePoint());
            partDto.setMagicPoint(npcPart.getPart().getMagicPoint());
            return partDto;
        }).collect(Collectors.toList()));

        dto.setSkills(npc.getSkills().stream().map(npcSkill -> {
            NpcDetailDto.SkillDto skillDto = new NpcDetailDto.SkillDto();
            skillDto.setName(npcSkill.getSkill().getName());
            skillDto.setContent(npcSkill.getSkill().getContent());
            return skillDto;
        }).collect(Collectors.toList()));

        dto.setBootys(npc.getBootys().stream().map(npcBooty -> {
            NpcDetailDto.BootyDto bootyDto = new NpcDetailDto.BootyDto();
            bootyDto.setDiceNum(npcBooty.getBooty().getDiceNum());
            bootyDto.setContent(npcBooty.getBooty().getContent());
            return bootyDto;
        }).collect(Collectors.toList()));

        return dto;
    }
}