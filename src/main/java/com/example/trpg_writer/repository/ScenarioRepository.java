package com.example.trpg_writer.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.User;

public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {
    Page<Scenario> findByUser(User user , Pageable pageable);
}
