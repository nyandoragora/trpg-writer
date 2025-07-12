package com.example.trpg_writer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.entity.Scenario;

public interface SceneRepository extends JpaRepository<Scene, Integer> {
    Page<Scene> findByScenarioOrderByCreatedAtAsc(Scenario scenario , Pageable pageable);
}
