package com.example.trpg_writer.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.trpg_writer.entity.User;

public class UserDetailsImpl implements UserDetails {
    private final User user;
    private final String name; // ユーザー名用のフィールドを追加
    private final Collection<GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.user = user;
        this.name = user.getName(); // コンストラクタでユーザー名を設定
        // ここでは単純に "ROLE_GENERAL" を付与する。必要に応じて変更する。
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_GENERAL"));
    }

    public User getUser() {
        return user;
    }

    // 画面表示用のユーザー名を返す
    public String getName() {
        return name;
    }

    // ハッシュ化済みのパスワードを返す
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // ログイン時に利用するユーザー名（メールアドレス）を返す
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // ロールのコレクションを返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // アカウントが期限切れでなければtrueを返す
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ユーザーがロックされていなければtrueを返す
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ユーザーのパスワードが期限切れでなければtrueを返す
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ユーザーが有効であればtrueを返す
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
