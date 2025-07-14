package com.example.trpg_writer.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.ScenarioForm;
import com.example.trpg_writer.repository.ScenarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScenarioService {

  private final ScenarioRepository scenarioRepository;

  @Transactional
  public void create(ScenarioForm scenarioForm, User user) {
      Scenario scenario = new Scenario();
      scenario.setTitle(scenarioForm.getTitle());
      scenario.setIntroduction(scenarioForm.getIntroduction());
      scenario.setUser(user);
      scenarioRepository.save(scenario);
  }

  // ユーザーIDと合致するシナリオを出力する
  public Page<Scenario> findByUser(User user , Pageable pageable) {
    return scenarioRepository.findByUser(user , pageable);
  }

  public Optional<Scenario> findById(Integer id) {
    return scenarioRepository.findById(id);
  }

}
