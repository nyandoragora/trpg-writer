package com.example.trpg_writer.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.repository.ScenarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScenarioService {

  private final ScenarioRepository scenarioRepository;

  // ユーザーIDと合致するシナリオを出力する
  public Page<Scenario> findByUser(User user , Pageable pageable) {
    return scenarioRepository.findByUser(user , pageable);
  }

  public Optional<Scenario> findById(Integer id) {
    return scenarioRepository.findById(id);
  }

}
