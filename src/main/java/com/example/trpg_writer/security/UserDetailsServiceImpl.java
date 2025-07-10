package com.example.trpg_writer.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
        }
        System.out.println("User found: " + user.getEmail() + ", Enabled: " + user.getEnabled()); // デバッグログ追加
        System.out.println("User password (hashed): " + user.getPassword()); // 注意: 本番環境では絶対に行わないでください

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        return new UserDetailsImpl(user, authorities);
    }
}