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

import com.example.trpg_writer.entity.Part;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.form.PartForm;
import com.example.trpg_writer.repository.PartRepository;
import com.example.trpg_writer.repository.ScenarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("PartController テスト")
public class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオに部位を正常に作成できること")
    void whenCreatePartForOwnScenario_thenSucceeds() throws Exception {
        long countBefore = partRepository.count();
        PartForm partForm = new PartForm();
        partForm.setScenarioId(1); // taro.yamada's scenario
        partForm.setName("テスト部位");
        partForm.setHit(10);
        partForm.setDamage("1d6");
        partForm.setEvasion(5);
        partForm.setProtection(2);
        partForm.setLifePoint(20);
        partForm.setMagicPoint(0);

        mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("テスト部位"));

        long countAfter = partRepository.count();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオに部位を作成しようとすると404エラーになること")
    void whenCreatePartForOthersScenario_thenReturnsNotFound() throws Exception {
        long countBefore = partRepository.count();
        PartForm partForm = new PartForm();
        partForm.setScenarioId(1); // taro.yamada's scenario
        partForm.setName("不正な部位");
        partForm.setHit(10);
        partForm.setDamage("1d6");
        partForm.setEvasion(5);
        partForm.setProtection(2);
        partForm.setLifePoint(20);
        partForm.setMagicPoint(0);

        mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partForm))
                .with(csrf()))
                .andExpect(status().isNotFound());

        long countAfter = partRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("バリデーションエラーで部位の作成に失敗すること")
    void whenCreatePartWithInvalidData_thenReturnsBadRequest() throws Exception {
        long countBefore = partRepository.count();
        PartForm partForm = new PartForm();
        partForm.setScenarioId(1);
        partForm.setName(""); // Invalid: name is blank
        partForm.setHit(10);
        partForm.setDamage("1d6");
        partForm.setEvasion(5);
        partForm.setProtection(2);
        partForm.setLifePoint(20);
        partForm.setMagicPoint(0);


        mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partForm))
                .with(csrf()))
                .andExpect(status().isBadRequest());

        long countAfter = partRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオの部位を正常に削除できること")
    void whenDeletePartFromOwnScenario_thenSucceeds() throws Exception {
        // Create a new part for this test to avoid foreign key constraints
        Scenario scenario = scenarioRepository.findById(1).get(); // Get the scenario with its owner
        Part testPart = new Part();
        testPart.setScenario(scenario);
        testPart.setName("削除用部位");
        testPart.setHit(1);
        testPart.setDamage("1");
        testPart.setEvasion(1);
        testPart.setProtection(1);
        testPart.setLifePoint(1);
        testPart.setMagicPoint(1);
        Part savedPart = partRepository.save(testPart);

        long countBefore = partRepository.count();

        mockMvc.perform(delete("/api/parts/" + savedPart.getId())
                .with(csrf()))
                .andExpect(status().isOk());

        long countAfter = partRepository.count();
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオの部位を削除しようとすると404エラーになること")
    void whenDeletePartFromOthersScenario_thenReturnsNotFound() throws Exception {
        long countBefore = partRepository.count();

        mockMvc.perform(delete("/api/parts/1") // Part ID 1 belongs to Scenario ID 1 (taro.yamada)
                .with(csrf()))
                .andExpect(status().isNotFound());

        long countAfter = partRepository.count();
        assertEquals(countBefore, countAfter);
    }
}