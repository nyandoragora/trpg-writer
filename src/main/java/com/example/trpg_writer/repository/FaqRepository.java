package com.example.trpg_writer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.trpg_writer.entity.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {
}
