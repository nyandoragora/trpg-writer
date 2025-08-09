package com.example.trpg_writer.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("SceneNpcControllerのテスト")
public class SceneNpcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // --- Add NPC to Scene ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオのシーンにNPCを正常に追加できる")
    void whenAddNpcToOwnScene_thenSucceeds() throws Exception {
        // NPC ID 2 is not in Scene ID 1 initially
        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/2/add")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオのシーンにNPCを追加しようとすると404エラー")
    void whenAddNpcToOthersScene_thenReturnsNotFound() throws Exception {
        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/2/add")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // --- Remove NPC from Scene ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオのシーンからNPCを正常に削除できる")
    void whenRemoveNpcFromOwnScene_thenSucceeds() throws Exception {
        // SceneNpc ID 1 exists in data.sql
        mockMvc.perform(post("/scenarios/1/scenes/1/scene-npcs/1/remove")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオのシーンからNPCを削除しようとすると404エラー")
    void whenRemoveNpcFromOthersScene_thenReturnsNotFound() throws Exception {
        mockMvc.perform(post("/scenarios/1/scenes/1/scene-npcs/1/remove")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
