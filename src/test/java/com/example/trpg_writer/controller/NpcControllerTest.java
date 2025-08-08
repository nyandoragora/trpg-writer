package com.example.trpg_writer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.repository.NpcRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("NpcControllerのテスト")
public class NpcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NpcRepository npcRepository;

    // --- Create ---
    @Test
    @DisplayName("認証済みユーザーはNPC作成ページを正常に表示できる")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAuthenticatedUserAccessesCreatePage_thenReturnsCreateView() throws Exception {
        mockMvc.perform(get("/scenarios/1/scenes/1/npcs/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("npcs/create"));
    }

    @Test
    @DisplayName("未認証ユーザーがNPC作成ページにアクセスするとログインページにリダイレクトされること")
    public void whenUnauthenticatedUserAccessesCreatePage_thenRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/scenarios/1/scenes/1/npcs/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("他人のシナリオのNPC作成ページにアクセスすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenAccessOthersNpcCreatePage_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/scenes/1/npcs/create"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("NPCが正常に登録され、DBに保存されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenCreateNpcWithValidData_thenRedirectsToSceneEditPage() throws Exception {
        long countBefore = npcRepository.count();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/create")
                .param("name", "ゴブリン")
                .param("level", "2")
                .param("intelligence", "人間並み")
                .param("perception", "五感")
                .param("position", "なし")
                .param("language", "ゴブリン語")
                .param("popularity", "なし")
                .param("weakness", "なし")
                .param("preemptive", "10")
                .param("movement", "15")
                .param("lifeResist", "10")
                .param("mindResist", "10")
                .param("impurity", "5")
                .param("habitat", "草原")
                .param("description", "テスト用の説明文")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scenarios/1/scenes/1/edit"))
                .andExpect(flash().attribute("successMessage", "NPCを登録しました。"));

        long countAfter = npcRepository.count();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    @DisplayName("他人のシナリオにNPCを作成しようとすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenCreateNpcForOthersScenario_thenReturnsNotFound() throws Exception {
        long countBefore = npcRepository.count();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/create")
                .param("name", "不正なゴブリン")
                .with(csrf()))
                .andExpect(status().isNotFound());

        long countAfter = npcRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @DisplayName("名前が空のためNPC登録が失敗し、DBに保存されないこと")
    @WithUserDetails("taro.yamada@example.com")
    public void whenCreateNpcWithEmptyName_thenReturnsCreateView() throws Exception {
        long countBefore = npcRepository.count();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/create")
                .param("name", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("npcs/create"))
                .andExpect(model().attributeHasFieldErrors("npcForm", "name"));

        long countAfter = npcRepository.count();
        assertEquals(countBefore, countAfter);
    }

    // --- Read ---
    @Test
    @DisplayName("存在するNPCの詳細がJSONで正しく返されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenGetExistingNpcDetails_thenReturnsNpcJson() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ゴブリン"))
                .andExpect(jsonPath("$.level").value(1));
    }

    @Test
    @DisplayName("他人のシナリオのNPC詳細を要求すると404エラーが返されること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenGetOthersNpcDetails_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/1/details"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("存在しないNPCの詳細を要求すると404エラーが返されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenGetNonExistingNpcDetails_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/-1/details"))
                .andExpect(status().isNotFound());
    }

    // --- Delete ---
    @Test
    @DisplayName("存在するNPCを正常に削除できること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenDeleteExistingNpc_thenReturnsOk() throws Exception {
        long countBefore = npcRepository.count();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/1/delete")
                .with(csrf()))
                .andExpect(status().isOk());

        long countAfter = npcRepository.count();
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    @DisplayName("他人のシナリオのNPCを削除しようとすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenDeleteOthersNpc_thenReturnsNotFound() throws Exception {
        long countBefore = npcRepository.count();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/1/delete")
                .with(csrf()))
                .andExpect(status().isNotFound());
        
        long countAfter = npcRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @DisplayName("存在しないNPCを削除しようとしてもエラーにならず、DBの件数が変わらないこと")
    @WithUserDetails("taro.yamada@example.com")
    public void whenDeleteNonExistingNpc_thenReturnsOk() throws Exception {
        long countBefore = npcRepository.count();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/-1/delete")
                .with(csrf()))
                .andExpect(status().isOk());

        long countAfter = npcRepository.count();
        assertEquals(countBefore, countAfter);
    }

    // --- Edit ---
    @Test
    @DisplayName("NPC編集ページが正常に表示されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAccessEditPageWithValidId_thenReturnsEditView() throws Exception {
        mockMvc.perform(get("/scenarios/1/scenes/1/npcs/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("npcs/edit"))
                .andExpect(model().attributeExists("npcForm"));
    }

    @Test
    @DisplayName("存在しないNPCの編集ページにアクセスすると404エラーが発生すること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAccessEditPageWithInvalidId_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/scenes/1/npcs/-1/edit"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("他人のNPC編集ページにアクセスすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenAccessOthersNpcEditPage_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/scenes/1/npcs/1/edit"))
                .andExpect(status().isNotFound());
    }

    // --- Update ---
    @Test
    @DisplayName("NPCが正常に更新され、DBの値が変更されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenUpdateNpcWithValidData_thenRedirectsToSceneEditPage() throws Exception {
        Npc npcBefore = npcRepository.findById(1).orElseThrow();
        String nameBefore = npcBefore.getName();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/1/update")
                .param("name", "ゴブリンリーダー")
                .param("level", "3")
                .param("intelligence", "人間並み")
                .param("perception", "五感")
                .param("position", "なし")
                .param("language", "ゴブリン語")
                .param("popularity", "なし")
                .param("weakness", "なし")
                .param("preemptive", "10")
                .param("movement", "15")
                .param("lifeResist", "10")
                .param("mindResist", "10")
                .param("impurity", "5")
                .param("habitat", "草原")
                .param("description", "テスト用の説明文")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scenarios/1/scenes/1/edit"))
                .andExpect(flash().attribute("successMessage", "NPCを更新しました。"));

        Npc npcAfter = npcRepository.findById(1).orElseThrow();
        String nameAfter = npcAfter.getName();
        assertNotEquals(nameBefore, nameAfter);
        assertEquals("ゴブリンリーダー", nameAfter);
    }

    @Test
    @DisplayName("他人のシナリオのNPCを更新しようとすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenUpdateOthersNpc_thenReturnsNotFound() throws Exception {
        Npc npcBefore = npcRepository.findById(1).orElseThrow();
        String nameBefore = npcBefore.getName();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/1/update")
                .param("name", "不正なゴブリンリーダー")
                .with(csrf()))
                .andExpect(status().isNotFound());

        Npc npcAfter = npcRepository.findById(1).orElseThrow();
        String nameAfter = npcAfter.getName();
        assertEquals(nameBefore, nameAfter);
    }

    @Test
    @DisplayName("名前が空のためNPC更新が失敗し、DBの値が変更されないこと")
    @WithUserDetails("taro.yamada@example.com")
    public void whenUpdateNpcWithEmptyName_thenReturnsEditView() throws Exception {
        Npc npcBefore = npcRepository.findById(1).orElseThrow();
        String nameBefore = npcBefore.getName();

        mockMvc.perform(post("/scenarios/1/scenes/1/npcs/1/update")
                .param("name", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("npcs/edit"))
                .andExpect(model().attributeHasFieldErrors("npcForm", "name"));

        Npc npcAfter = npcRepository.findById(1).orElseThrow();
        String nameAfter = npcAfter.getName();
        assertEquals(nameBefore, nameAfter);
    }
}