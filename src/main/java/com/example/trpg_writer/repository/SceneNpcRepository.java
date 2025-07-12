package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.SceneNpc;
import com.example.trpg_writer.entity.SceneNpcId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SceneNpcRepository extends JpaRepository<SceneNpc, SceneNpcId> {
}
