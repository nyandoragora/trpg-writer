package com.example.trpg_writer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trpg_writer.entity.User;

public interface UserRepository extends JpaRepository<User , Integer>{

  public User findByEmail(String email);

}
