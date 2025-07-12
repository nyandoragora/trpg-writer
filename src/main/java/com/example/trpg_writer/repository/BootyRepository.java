package com.example.trpg_writer.repository;

import com.example.trpg_writer.entity.Booty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BootyRepository extends JpaRepository<Booty, Integer> {
}
