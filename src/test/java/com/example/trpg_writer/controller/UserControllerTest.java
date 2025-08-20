package com.example.trpg_writer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("UserControllerのテスト")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithUserDetails(value = "taro.yamada@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("マイページが正しく表示されること")
    public void testMyPage() throws Exception {
        mockMvc.perform(get("/users/my-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/index"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithUserDetails(value = "taro.yamada@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("プロフィール編集ページが正しく表示されること")
    public void testEditPage() throws Exception {
        mockMvc.perform(get("/users/my-page/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/edit"))
                .andExpect(model().attributeExists("userEditForm"));
    }

    @Test
    @WithUserDetails(value = "taro.yamada@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("プロフィールの更新が成功すること")
    public void testUpdateProfileSuccess() throws Exception {
        doNothing().when(userService).update(any(), any(), any(User.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "新しい名前");
        params.add("introduction", "新しい自己紹介");

        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test data".getBytes());

        mockMvc.perform(multipart("/users/my-page/update")
                .file(imageFile)
                .params(params)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/my-page"));

        verify(userService, times(1)).update(any(), any(), any(User.class));
    }

    @Test
    @WithUserDetails(value = "taro.yamada@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("プロフィールの更新でバリデーションエラーが発生すること")
    public void testUpdateProfileValidationError() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", ""); // 名前を空にする
        params.add("introduction", "自己紹介");

        // 中身が空でも、imageFileパートをリクエストに含めることが重要
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "", "application/octet-stream", new byte[0]);

        mockMvc.perform(multipart("/users/my-page/update")
                .file(imageFile)
                .params(params)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/edit"));

        verify(userService, times(0)).update(any(), any(), any(User.class));
    }

    @Test
    @WithUserDetails(value = "taro.yamada@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("アカウントの削除が成功すること")
    public void testDeleteAccount() throws Exception {
        doNothing().when(userService).delete(any(User.class));

        mockMvc.perform(post("/users/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/logout"));

        verify(userService, times(1)).delete(any(User.class));
    }
}
