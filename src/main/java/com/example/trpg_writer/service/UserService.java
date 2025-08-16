package com.example.trpg_writer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.SignupForm;
import com.example.trpg_writer.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(SignupForm signupForm) {
        User user = new User();

        user.setName(signupForm.getName());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setEnabled(true); // enabled を true に設定

        return userRepository.save(user);
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
