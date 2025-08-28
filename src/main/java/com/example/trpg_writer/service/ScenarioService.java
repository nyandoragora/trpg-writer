package com.example.trpg_writer.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.ScenarioForm;
import com.example.trpg_writer.repository.ScenarioRepository;
import com.example.trpg_writer.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScenarioService {

  private final ScenarioRepository scenarioRepository;

  @Transactional
  public Scenario create(ScenarioForm scenarioForm, User user) {
      Scenario scenario = new Scenario();
      scenario.setTitle(scenarioForm.getTitle());
      scenario.setIntroduction(scenarioForm.getIntroduction());
      scenario.setUser(user);
      return scenarioRepository.save(scenario);
  }

  @Transactional
  public void update(ScenarioForm scenarioForm) {
      Scenario scenario = scenarioRepository.findById(scenarioForm.getId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
      scenario.setTitle(scenarioForm.getTitle());
      scenario.setIntroduction(scenarioForm.getIntroduction());
      scenarioRepository.save(scenario);
  }

  @Transactional
  public void delete(Integer id) {
      scenarioRepository.deleteById(id);
  }

  // ユーザーIDと合致するシナリオを出力する
  public Page<Scenario> findByUser(User user , Pageable pageable) {
    return scenarioRepository.findByUser(user , pageable);
  }

  public Optional<Scenario> findById(Integer id) {
    return scenarioRepository.findById(id);
  }

  public void checkScenarioOwnership(Integer scenarioId, UserDetailsImpl userDetails) {
      Scenario scenario = findById(scenarioId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
      if (userDetails == null || !scenario.getUser().getId().equals(userDetails.getUser().getId())) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found");
      }
  }

}
