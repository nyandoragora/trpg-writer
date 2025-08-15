package com.example.trpg_writer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.form.NpcForm;
import com.example.trpg_writer.repository.NpcRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Autowired
    private ObjectMapper objectMapper;

    // --- Page Rendering ---
    @Test
    @DisplayName("認証済みユーザーはNPC作成ページを正常に表示できる")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAuthenticatedUserAccessesCreatePage_thenReturnsCreateView() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/create").param("sceneId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("npcs/create"));
    }

    @Test
    @DisplayName("未認証ユーザーがNPC作成ページにアクセスするとログインページにリダイレクトされること")
    public void whenUnauthenticatedUserAccessesCreatePage_thenRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("他人のシナリオのNPC作成ページにアクセスすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenAccessOthersNpcCreatePage_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/create"))
                .andExpect(status().isNotFound());
    }

    // --- API Create ---
    @Test
    @DisplayName("API: NPCが正常に登録され、DBに保存されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenCreateNpcWithValidData_thenReturnsCreated() throws Exception {
        long countBefore = npcRepository.count();
        NpcForm npcForm = new NpcForm();
        npcForm.setScenarioId(1);
        npcForm.setName("テストNPC");
        npcForm.setLevel(1);
        npcForm.setIntelligence("動物並み");
        npcForm.setPerception("五感");
        npcForm.setPosition("なし");
        npcForm.setImpurity(0);
        npcForm.setLanguage("なし");
        npcForm.setHabitat("森林");
        npcForm.setPopularity("なし");
        npcForm.setPreemptive(10);
        npcForm.setMovement("10");
        npcForm.setLifeResist(10);
        npcForm.setMindResist(10);

        mockMvc.perform(post("/scenarios/1/api/npcs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(npcForm))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("テストNPC"));

        long countAfter = npcRepository.count();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    @DisplayName("API: 他人のシナリオにNPCを作成しようとすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenCreateNpcForOthersScenario_thenReturnsNotFound() throws Exception {
        long countBefore = npcRepository.count();
        NpcForm npcForm = new NpcForm();
        npcForm.setName("不正なNPC");

        mockMvc.perform(post("/scenarios/1/api/npcs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(npcForm))
                .with(csrf()))
                .andExpect(status().isNotFound());

        long countAfter = npcRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @DisplayName("API: 名前が空のためNPC登録が失敗し、DBに保存されないこと")
    @WithUserDetails("taro.yamada@example.com")
    public void whenCreateNpcWithEmptyName_thenReturnsBadRequest() throws Exception {
        long countBefore = npcRepository.count();
        NpcForm npcForm = new NpcForm();
        npcForm.setName("");

        mockMvc.perform(post("/scenarios/1/api/npcs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(npcForm))
                .with(csrf()))
                .andExpect(status().isBadRequest());

        long countAfter = npcRepository.count();
        assertEquals(countBefore, countAfter);
    }

    // --- API Read ---
    @Test
    @DisplayName("存在するNPCの詳細がJSONで正しく返されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenGetExistingNpcDetails_thenReturnsNpcJson() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ゴブリン"))
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.parts").isArray())
                .andExpect(jsonPath("$.skills").isArray())
                .andExpect(jsonPath("$.bootys").isArray());
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

    // --- API Delete ---
    @Test
    @DisplayName("存在するNPCを正常に削除できること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenDeleteExistingNpc_thenReturnsOk() throws Exception {
        long countBefore = npcRepository.count();

        mockMvc.perform(delete("/scenarios/1/npcs/1")
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

        mockMvc.perform(delete("/scenarios/1/npcs/1")
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

        mockMvc.perform(delete("/scenarios/1/npcs/-1")
                .with(csrf()))
                .andExpect(status().isOk());

        long countAfter = npcRepository.count();
        assertEquals(countBefore, countAfter);
    }

    // --- Page Rendering (Edit) ---
    @Test
    @DisplayName("NPC編集ページが正常に表示されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAccessEditPageWithValidId_thenReturnsEditView() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/1/edit").param("sceneId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("npcs/edit"))
                .andExpect(model().attributeExists("npcForm"));
    }

    @Test
    @DisplayName("存在しないNPCの編集ページにアクセスすると404エラーが発生すること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAccessEditPageWithInvalidId_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/-1/edit"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("他人のNPC編集ページにアクセスすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenAccessOthersNpcEditPage_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/npcs/1/edit"))
                .andExpect(status().isNotFound());
    }

    // --- API Update ---
    @Test
    @DisplayName("API: NPCが正常に更新され、DBの値が変更されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenUpdateNpcWithValidData_thenReturnsOk() throws Exception {
        Npc npcBefore = npcRepository.findById(1).orElseThrow();
        String nameBefore = npcBefore.getName();

        NpcForm npcForm = new NpcForm();
        npcForm.setScenarioId(1);
        npcForm.setName("ゴブリンリーダー");
        npcForm.setLevel(3);
        npcForm.setIntelligence("人間並み");
        npcForm.setPerception("五感");
        npcForm.setPosition("なし");
        npcForm.setImpurity(5);
        npcForm.setLanguage("ゴブリン語");
        npcForm.setHabitat("洞窟");
        npcForm.setPopularity("なし");
        npcForm.setPreemptive(12);
        npcForm.setMovement("15");
        npcForm.setLifeResist(12);
        npcForm.setMindResist(12);

        mockMvc.perform(put("/scenarios/1/api/npcs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(npcForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ゴブリンリーダー"));

        Npc npcAfter = npcRepository.findById(1).orElseThrow();
        String nameAfter = npcAfter.getName();
        assertNotEquals(nameBefore, nameAfter);
        assertEquals("ゴブリンリーダー", nameAfter);
    }

    @Test
    @DisplayName("API: 他人のシナリオのNPCを更新しようとすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenUpdateOthersNpc_thenReturnsNotFound() throws Exception {
        Npc npcBefore = npcRepository.findById(1).orElseThrow();
        String nameBefore = npcBefore.getName();
        
        NpcForm npcForm = new NpcForm();
        npcForm.setName("不正なゴブリンリーダー");

        mockMvc.perform(put("/scenarios/1/api/npcs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(npcForm))
                .with(csrf()))
                .andExpect(status().isNotFound());

        Npc npcAfter = npcRepository.findById(1).orElseThrow();
        String nameAfter = npcAfter.getName();
        assertEquals(nameBefore, nameAfter);
    }

    @Test
    @DisplayName("API: 名前が空のためNPC更新が失敗し、DBの値が変更されないこと")
    @WithUserDetails("taro.yamada@example.com")
    public void whenUpdateNpcWithEmptyName_thenReturnsBadRequest() throws Exception {
        Npc npcBefore = npcRepository.findById(1).orElseThrow();
        String nameBefore = npcBefore.getName();

        NpcForm npcForm = new NpcForm();
        npcForm.setName("");

        mockMvc.perform(put("/scenarios/1/api/npcs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(npcForm))
                .with(csrf()))
                .andExpect(status().isBadRequest());

        Npc npcAfter = npcRepository.findById(1).orElseThrow();
        String nameAfter = npcAfter.getName();
        assertEquals(nameBefore, nameAfter);
    }
}
