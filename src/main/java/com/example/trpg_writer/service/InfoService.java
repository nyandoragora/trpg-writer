package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Info;
import com.example.trpg_writer.repository.InfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository infoRepository;

    public Optional<Info> findById(Integer id) {
        return infoRepository.findById(id);
    }

    public List<Info> findByScenarioId(Integer scenarioId) {
        return infoRepository.findByScenarioId(scenarioId);
    }
}
