package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.Info;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRepository extends JpaRepository<Info, Integer> {
    List<Info> findByScenarioId(Integer scenarioId);
}
