package com.example.trpg_writer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.trpg_writer.dto.SceneEditPageData;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.form.SceneForm;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneDataService;
import com.example.trpg_writer.service.SceneService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("SceneControllerのテスト")
public class SceneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScenarioService scenarioService;

    @MockBean
    private SceneService sceneService;

    @MockBean
    private SceneDataService sceneDataService;

    private Scenario scenario;
    private Scene scene;

    @BeforeEach
    void setUp() {
        // 共通のモック設定
        scenario = new Scenario();
        scenario.setId(1);
        scene = new Scene();
        scene.setId(1);

        when(scenarioService.findById(1)).thenReturn(Optional.of(scenario));
        when(sceneService.create(any(SceneForm.class), anyInt())).thenReturn(scene);
        doNothing().when(scenarioService).checkScenarioOwnership(anyInt(), any());
    }

    // --- Create ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオのシーン作成ページを正常に表示できる")
    void whenAccessOwnSceneCreatePage_thenSucceeds() throws Exception {
        mockMvc.perform(get("/scenarios/1/scenes/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("scenarios/scenes/create"))
                .andExpect(model().attributeExists("scenario"));
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオのシーン作成ページにアクセスすると404エラー")
    void whenAccessOthersSceneCreatePage_thenReturnsNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(scenarioService).checkScenarioOwnership(anyInt(), any());

        mockMvc.perform(get("/scenarios/1/scenes/create"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオにシーンを正常に作成できる")
    void whenCreateSceneForOwnScenario_thenSucceeds() throws Exception {
        mockMvc.perform(post("/scenarios/1/scenes/create")
                .param("title", "新しいシーン")
                .param("content", "テストコンテント")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scenarios/1/scenes/1/edit"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオにシーンを作成しようとすると404エラー")
    void whenCreateSceneForOthersScenario_thenReturnsNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(scenarioService).checkScenarioOwnership(anyInt(), any());

        mockMvc.perform(post("/scenarios/1/scenes/create")
                .param("title", "不正なシーン")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("タイトルを空にしてシーンを作成しようとすると失敗する")
    void whenCreateSceneWithEmptyTitle_thenFails() throws Exception {
        mockMvc.perform(post("/scenarios/1/scenes/create")
                .param("title", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("scenarios/scenes/create"))
                .andExpect(model().attributeHasFieldErrors("sceneForm", "title"));
    }

    // --- Edit ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオのシーン編集ページを正常に表示できる")
    void whenAccessOwnSceneEditPage_thenSucceeds() throws Exception {
        mockMvc.perform(get("/scenarios/1/scenes/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("scenarios/scenes/edit"))
                .andExpect(model().attribute("scenarioId", 1))
                .andExpect(model().attribute("sceneId", 1));
    }

    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("シーン編集ページのデータ取得APIが正常に動作する")
    void whenGetEditPageData_thenSucceeds() throws Exception {
        SceneEditPageData mockData = new SceneEditPageData(); // Populate with test data if needed
        when(sceneDataService.getSceneEditPageData(1, 1)).thenReturn(mockData);

        mockMvc.perform(get("/scenarios/1/scenes/1/edit-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }


    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオのシーン編集ページにアクセスすると404エラー")
    void whenAccessOthersSceneEditPage_thenReturnsNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(scenarioService).checkScenarioOwnership(anyInt(), any());

        mockMvc.perform(get("/scenarios/1/scenes/1/edit"))
                .andExpect(status().isNotFound());
    }

    // --- Update ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオのシーンを正常に更新できる")
    void whenUpdateOwnScene_thenSucceeds() throws Exception {
        mockMvc.perform(post("/scenarios/1/scenes/1/update")
                .param("title", "更新されたシーン")
                .param("content", "更新されたコンテント")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scenarios/1/scenes/1/edit"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオのシーンを更新しようとすると404エラー")
    void whenUpdateOthersScene_thenReturnsNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(scenarioService).checkScenarioOwnership(anyInt(), any());

        mockMvc.perform(post("/scenarios/1/scenes/1/update")
                .param("title", "不正な更新")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // --- Upload Image ---
    @Test
    @WithUserDetails("taro.yamada@example.com")
    @DisplayName("自分のシナリオのシーンに画像をアップロードできる")
    void whenUploadImageForOwnScene_thenSucceeds() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test image".getBytes());
        mockMvc.perform(multipart("/scenarios/1/scenes/1/uploadImage")
                .file(file)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scenarios/1/scenes/1/edit"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithUserDetails("hanako.suzuki@example.com")
    @DisplayName("他人のシナリオのシーンに画像をアップロードしようとすると404エラー")
    void whenUploadImageForOthersScene_thenReturnsNotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test image".getBytes());
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
            .when(scenarioService).checkScenarioOwnership(anyInt(), any());

        mockMvc.perform(multipart("/scenarios/1/scenes/1/uploadImage")
                .file(file)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
