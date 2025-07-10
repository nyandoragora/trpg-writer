package com.example.trpg_writer.service;

import org.springframework.stereotype.Service;

import com.example.trpg_writer.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository userRepository;

  
}
