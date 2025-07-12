package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Part;
import com.example.trpg_writer.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    public Optional<Part> findById(Integer id) {
        return partRepository.findById(id);
    }
}
