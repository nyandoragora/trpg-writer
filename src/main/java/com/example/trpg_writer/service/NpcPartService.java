package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.NpcPart;
import com.example.trpg_writer.entity.NpcPartId;
import com.example.trpg_writer.repository.NpcPartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NpcPartService {

    private final NpcPartRepository npcPartRepository;

    public Optional<NpcPart> findById(NpcPartId id) {
        return npcPartRepository.findById(id);
    }
}
