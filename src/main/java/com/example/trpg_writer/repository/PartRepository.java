package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.Part;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {
    List<Part> findByScenarioId(Integer scenarioId);
}
