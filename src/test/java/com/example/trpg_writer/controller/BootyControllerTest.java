package com.example.trpg_writer.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.trpg_writer.entity.Booty;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.form.BootyForm;
import com.example.trpg_writer.repository.BootyRepository;
import com.example.trpg_writer.repository.ScenarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("BootyController テスト")
public class BootyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BootyRepository bootyRepository;

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオに戦利品を正常に作成できること")
    void whenCreateBootyForOwnScenario_thenSucceeds() throws Exception {
        long countBefore = bootyRepository.count();
        BootyForm bootyForm = new BootyForm();
        bootyForm.setScenarioId(1); // taro.yamada's scenario
        bootyForm.setDiceNum("1d6");
        bootyForm.setContent("テスト戦利品");

        mockMvc.perform(post("/api/bootys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bootyForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.content").value("テスト戦利品"));

        long countAfter = bootyRepository.count();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオに戦利品を作成しようとすると404エラーになること")
    void whenCreateBootyForOthersScenario_thenReturnsNotFound() throws Exception {
        long countBefore = bootyRepository.count();
        BootyForm bootyForm = new BootyForm();
        bootyForm.setScenarioId(1); // taro.yamada's scenario
        bootyForm.setDiceNum("1d6");
        bootyForm.setContent("不正な戦利品");

        mockMvc.perform(post("/api/bootys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bootyForm))
                .with(csrf()))
                .andExpect(status().isNotFound());

        long countAfter = bootyRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("バリデーションエラーで戦利品の作成に失敗すること")
    void whenCreateBootyWithInvalidData_thenReturnsBadRequest() throws Exception {
        long countBefore = bootyRepository.count();
        BootyForm bootyForm = new BootyForm();
        bootyForm.setScenarioId(1);
        bootyForm.setContent(""); // Invalid: content is blank

        mockMvc.perform(post("/api/bootys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bootyForm))
                .with(csrf()))
                .andExpect(status().isBadRequest());

        long countAfter = bootyRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオの戦利品を正常に削除できること")
    void whenDeleteBootyFromOwnScenario_thenSucceeds() throws Exception {
        Scenario scenario = scenarioRepository.findById(1).get();
        Booty testBooty = new Booty();
        testBooty.setScenario(scenario);
        testBooty.setDiceNum("1d6");
        testBooty.setContent("削除用戦利品");
        Booty savedBooty = bootyRepository.save(testBooty);

        long countBefore = bootyRepository.count();

        mockMvc.perform(delete("/api/bootys/" + savedBooty.getId())
                .with(csrf()))
                .andExpect(status().isOk());

        long countAfter = bootyRepository.count();
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオの戦利品を削除しようとすると404エラーになること")
    void whenDeleteBootyFromOthersScenario_thenReturnsNotFound() throws Exception {
        long countBefore = bootyRepository.count();

        mockMvc.perform(delete("/api/bootys/1") // Booty ID 1 belongs to Scenario ID 1 (taro.yamada)
                .with(csrf()))
                .andExpect(status().isNotFound());

        long countAfter = bootyRepository.count();
        assertEquals(countBefore, countAfter);
    }
}