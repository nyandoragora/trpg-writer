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

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Skill;
import com.example.trpg_writer.form.SkillForm;
import com.example.trpg_writer.repository.ScenarioRepository;
import com.example.trpg_writer.repository.SkillRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("SkillController テスト")
public class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオに特技を正常に作成できること")
    void whenCreateSkillForOwnScenario_thenSucceeds() throws Exception {
        long countBefore = skillRepository.count();
        SkillForm skillForm = new SkillForm();
        skillForm.setScenarioId(1); // taro.yamada's scenario
        skillForm.setName("テスト特技");
        skillForm.setContent("テスト内容");

        mockMvc.perform(post("/api/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skillForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("テスト特技"));

        long countAfter = skillRepository.count();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオに特技を作成しようとすると404エラーになること")
    void whenCreateSkillForOthersScenario_thenReturnsNotFound() throws Exception {
        long countBefore = skillRepository.count();
        SkillForm skillForm = new SkillForm();
        skillForm.setScenarioId(1); // taro.yamada's scenario
        skillForm.setName("不正な特技");
        skillForm.setContent("不正な内容");

        mockMvc.perform(post("/api/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skillForm))
                .with(csrf()))
                .andExpect(status().isNotFound());

        long countAfter = skillRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("バリデーションエラーで特技の作成に失敗すること")
    void whenCreateSkillWithInvalidData_thenReturnsBadRequest() throws Exception {
        long countBefore = skillRepository.count();
        SkillForm skillForm = new SkillForm();
        skillForm.setScenarioId(1);
        skillForm.setName(""); // Invalid: name is blank

        mockMvc.perform(post("/api/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skillForm))
                .with(csrf()))
                .andExpect(status().isBadRequest());

        long countAfter = skillRepository.count();
        assertEquals(countBefore, countAfter);
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオの特技を正常に削除できること")
    void whenDeleteSkillFromOwnScenario_thenSucceeds() throws Exception {
        Scenario scenario = scenarioRepository.findById(1).get();
        Skill testSkill = new Skill();
        testSkill.setScenario(scenario);
        testSkill.setName("削除用特技");
        testSkill.setContent("削除用内容");
        Skill savedSkill = skillRepository.save(testSkill);

        long countBefore = skillRepository.count();

        mockMvc.perform(delete("/api/skills/" + savedSkill.getId())
                .with(csrf()))
                .andExpect(status().isOk());

        long countAfter = skillRepository.count();
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオの特技を削除しようとすると404エラーになること")
    void whenDeleteSkillFromOthersScenario_thenReturnsNotFound() throws Exception {
        long countBefore = skillRepository.count();

        mockMvc.perform(delete("/api/skills/1") // Skill ID 1 belongs to Scenario ID 1 (taro.yamada)
                .with(csrf()))
                .andExpect(status().isNotFound());

        long countAfter = skillRepository.count();
        assertEquals(countBefore, countAfter);
    }
}