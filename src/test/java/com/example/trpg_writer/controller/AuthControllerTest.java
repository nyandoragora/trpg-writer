package com.example.trpg_writer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("AuthControllerのテスト")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("ログインページが正常に表示されること")
    public void whenGetLoginPage_thenReturnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @DisplayName("正しい認証情報でログインが成功すること")
    void whenLoginWithValidCredentials_thenRedirectsToHomeAndAuthenticates() throws Exception {
        mockMvc.perform(formLogin("/login").user("taro.yamada@example.com").password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("taro.yamada@example.com"));
    }

    @Test
    @DisplayName("不正な認証情報でログインが失敗すること")
    void whenLoginWithInvalidCredentials_thenRedirectsToLoginErrorAndRemainsUnauthenticated() throws Exception {
        mockMvc.perform(formLogin("/login").user("taro.yamada@example.com").password("wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("ログアウトが成功すること")
    void whenLogout_thenRedirectsToLoginAndUnauthenticates() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?loggedOut"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("会員登録が成功し、データベースにユーザーが登録されること")
    public void whenSignupWithValidData_thenRedirectsToLoginAndUserIsCreated() throws Exception {
        mockMvc.perform(post("/signup")
                .param("name", "testuser")
                .param("email", "test@example.com")
                .param("password", "password123")
                .param("passwordConfirmation", "password123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("successMessage", "会員登録が完了しました。"));

        User savedUser = userRepository.findByEmail("test@example.com");
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getName());
    }

    @Test
    @DisplayName("パスワードが一致しないため会員登録が失敗し、データベースにユーザーが登録されないこと")
    public void whenSignupWithMismatchedPasswords_thenReturnsSignupViewAndUserIsNotCreated() throws Exception {
        long userCountBefore = userRepository.count();

        mockMvc.perform(post("/signup")
                .param("name", "testuser")
                .param("email", "fail@example.com")
                .param("password", "password123")
                .param("passwordConfirmation", "differentpassword")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signup"));

        long userCountAfter = userRepository.count();
        assertEquals(userCountBefore, userCountAfter);
    }
}