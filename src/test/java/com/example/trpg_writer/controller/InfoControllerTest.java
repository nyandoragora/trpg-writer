package com.example.trpg_writer.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.trpg_writer.form.InfoForm;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("InfoControllerのAPIテスト")
public class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- Store ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオに情報を正常に作成できる")
    void whenCreateInfoForOwnScenario_thenSucceeds() throws Exception {
        InfoForm infoForm = new InfoForm();
        infoForm.setName("新しい情報");
        infoForm.setContent("テストコンテント");
        infoForm.setSceneId(1);

        mockMvc.perform(post("/api/scenarios/1/infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(infoForm))
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオに情報を作成しようとすると404エラー")
    void whenCreateInfoForOthersScenario_thenReturnsNotFound() throws Exception {
        InfoForm infoForm = new InfoForm();
        infoForm.setName("不正な情報");
        infoForm.setSceneId(1);

        mockMvc.perform(post("/api/scenarios/1/infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(infoForm))
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("名前を空にして情報を作成しようとすると400エラー")
    void whenCreateInfoWithEmptyName_thenFails() throws Exception {
        InfoForm infoForm = new InfoForm();
        infoForm.setName(""); // Invalid
        infoForm.setSceneId(1);

        mockMvc.perform(post("/api/scenarios/1/infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(infoForm))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    // --- Update ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオの情報を正常に更新できる")
    void whenUpdateOwnInfo_thenSucceeds() throws Exception {
        InfoForm infoForm = new InfoForm();
        infoForm.setName("更新された情報");
        infoForm.setContent("更新されたコンテント");
        infoForm.setSceneId(1);

        mockMvc.perform(put("/api/scenarios/1/infos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(infoForm))
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオの情報を更新しようとすると404エラー")
    void whenUpdateOthersInfo_thenReturnsNotFound() throws Exception {
        InfoForm infoForm = new InfoForm();
        infoForm.setName("不正な更新");
        infoForm.setSceneId(1);

        mockMvc.perform(put("/api/scenarios/1/infos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(infoForm))
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // --- Delete ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオの情報を正常に削除できる")
    void whenDeleteOwnInfo_thenSucceeds() throws Exception {
        mockMvc.perform(delete("/api/scenarios/1/infos/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオの情報を削除しようとすると404エラー")
    void whenDeleteOthersInfo_thenReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/scenarios/1/infos/1")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
}