package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Skill;
import com.example.trpg_writer.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public Optional<Skill> findById(Integer id) {
        return skillRepository.findById(id);
    }
}
