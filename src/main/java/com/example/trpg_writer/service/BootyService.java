package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Booty;
import com.example.trpg_writer.repository.BootyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BootyService {

    private final BootyRepository bootyRepository;

    public Optional<Booty> findById(Integer id) {
        return bootyRepository.findById(id);
    }
}
