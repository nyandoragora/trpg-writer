package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.NpcSkill;
import com.example.trpg_writer.entity.NpcSkillId;
import com.example.trpg_writer.repository.NpcSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NpcSkillService {

    private final NpcSkillRepository npcSkillRepository;

    public Optional<NpcSkill> findById(NpcSkillId id) {
        return npcSkillRepository.findById(id);
    }
}
