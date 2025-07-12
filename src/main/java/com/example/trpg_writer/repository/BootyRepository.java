package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.Booty;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BootyRepository extends JpaRepository<Booty, Integer> {
    List<Booty> findByScenarioId(Integer scenarioId);
}
