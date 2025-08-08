package com.example.trpg_writer.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.repository.ScenarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("ScenarioControllerのテスト")
public class ScenarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScenarioRepository scenarioRepository;

    // --- Create ---
    @Test
    @DisplayName("認証済みユーザーはシナリオ作成ページを正常に表示できる")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAuthenticatedUserAccessesCreatePage_thenReturnsCreateView() throws Exception {
        mockMvc.perform(get("/scenarios/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("scenarios/create"));
    }

    @Test
    @DisplayName("未認証ユーザーがシナリオ作成ページにアクセスするとログインページにリダイレクトされること")
    public void whenUnauthenticatedUserAccessesCreatePage_thenRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/scenarios/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("シナリオが正常に登録されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenCreateScenarioWithValidData_thenRedirectsToUsersPage() throws Exception {
        long countBefore = scenarioRepository.count();

        mockMvc.perform(post("/scenarios/create")
                .param("title", "新しいテストシナリオ")
                .param("introduction", "これはテスト用の導入文です。")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("successMessage", "シナリオを登録しました。"));

        long countAfter = scenarioRepository.count();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    @DisplayName("タイトルが空のためシナリオ登録が失敗すること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenCreateScenarioWithEmptyTitle_thenReturnsCreateView() throws Exception {
        long countBefore = scenarioRepository.count();

        mockMvc.perform(post("/scenarios/create")
                .param("title", "")
                .param("introduction", "これはテスト用の導入文です。")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("scenarios/create"))
                .andExpect(model().attributeHasFieldErrors("scenarioForm", "title"));

        long countAfter = scenarioRepository.count();
        assertEquals(countBefore, countAfter);
    }

    // --- Edit Page ---
    @Test
    @DisplayName("シナリオ編集ページが正常に表示されること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAccessEditPageWithValidId_thenReturnsEditView() throws Exception {
        mockMvc.perform(get("/scenarios/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("scenarios/edit"))
                .andExpect(model().attributeExists("scenario"))
                .andExpect(model().attributeExists("scenes"));
    }

    @Test
    @DisplayName("未認証ユーザーがシナリオ編集ページにアクセスするとログインページにリダイレクトされること")
    public void whenUnauthenticatedUserAccessesEditPage_thenRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/scenarios/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("存在しないシナリオの編集ページにアクセスすると404エラーが発生すること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenAccessEditPageWithInvalidId_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/-1/edit"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("他人のシナリオ編集ページにアクセスすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenAccessOthersScenarioEditPage_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/edit"))
                .andExpect(status().isNotFound());
    }

    // --- Update ---
    @Test
    @DisplayName("自分のシナリオを正常に更新できること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenUpdateOwnScenario_thenSucceeds() throws Exception {
        Scenario scenarioBefore = scenarioRepository.findById(1).get();
        String titleBefore = scenarioBefore.getTitle();

        mockMvc.perform(post("/scenarios/1/update")
                .param("title", "更新されたタイトル")
                .param("introduction", "更新された導入文")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scenarios/1/edit"))
                .andExpect(flash().attribute("successMessage", "シナリオを更新しました。"));

        Scenario scenarioAfter = scenarioRepository.findById(1).get();
        assertNotEquals(titleBefore, scenarioAfter.getTitle());
        assertEquals("更新されたタイトル", scenarioAfter.getTitle());
    }

    @Test
    @DisplayName("他人のシナリオを更新しようとすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenUpdateOthersScenario_thenReturnsNotFound() throws Exception {
        Scenario scenarioBefore = scenarioRepository.findById(1).get();
        String titleBefore = scenarioBefore.getTitle();

        mockMvc.perform(post("/scenarios/1/update")
                .param("title", "不正な更新")
                .with(csrf()))
                .andExpect(status().isNotFound());

        Scenario scenarioAfter = scenarioRepository.findById(1).get();
        assertEquals(titleBefore, scenarioAfter.getTitle());
    }

    @Test
    @DisplayName("タイトルを空にして更新しようとすると失敗すること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenUpdateScenarioWithEmptyTitle_thenFails() throws Exception {
        mockMvc.perform(post("/scenarios/1/update")
                .param("title", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("scenarios/edit"))
                .andExpect(model().attributeHasFieldErrors("scenarioForm", "title"));
    }

    // --- Delete ---
    @Test
    @DisplayName("自分のシナリオを正常に削除できること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenDeleteOwnScenario_thenSucceeds() throws Exception {
        assertTrue(scenarioRepository.existsById(1));
        mockMvc.perform(post("/scenarios/1/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("successMessage", "シナリオを削除しました。"));
        assertFalse(scenarioRepository.existsById(1));
    }

    @Test
    @DisplayName("他人のシナリオを削除しようとすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenDeleteOthersScenario_thenReturnsNotFound() throws Exception {
        assertTrue(scenarioRepository.existsById(1));
        mockMvc.perform(post("/scenarios/1/delete")
                .with(csrf()))
                .andExpect(status().isNotFound());
        assertTrue(scenarioRepository.existsById(1));
    }

    // --- PDF Download ---
    @Test
    @DisplayName("PDFが正常にダウンロードされること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenDownloadPdfWithValidId_thenReturnsPdf() throws Exception {
        String expectedFilename = java.net.URLEncoder.encode("古城の秘宝.pdf", java.nio.charset.StandardCharsets.UTF_8.toString());

        mockMvc.perform(get("/scenarios/1/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().string(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, containsString(expectedFilename)));
    }

    @Test
    @DisplayName("未認証ユーザーがPDFをダウンロードしようとするとログインページにリダイレクトされること")
    public void whenUnauthenticatedUserDownloadsPdf_thenRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/scenarios/1/pdf"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("PDFの内容が正しく生成されていること")
    @WithUserDetails("taro.yamada@example.com")
    public void whenDownloadPdfWithValidId_thenPdfContentIsCorrect() throws Exception {
        MvcResult result = mockMvc.perform(get("/scenarios/1/pdf"))
                .andExpect(status().isOk())
                .andReturn();

        byte[] pdfContent = result.getResponse().getContentAsByteArray();

        try (PDDocument document = Loader.loadPDF(pdfContent)) {
            // Level 1: Sanity Check
            assertTrue(document.getNumberOfPages() > 0, "PDF should have at least one page.");

            // Level 2: Text Content Check
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            assertTrue(text.contains("古城の秘宝"), "PDF should contain the scenario title.");
            assertTrue(text.contains("ゴブリン"), "PDF should contain an NPC name.");
            assertTrue(text.contains("城門"), "PDF should contain a scene title.");
        }
    }

    @Test
    @DisplayName("他人のシナリオPDFをダウンロードしようとすると404エラーが発生すること")
    @WithUserDetails("hanako.suzuki@example.com")
    public void whenDownloadOthersScenarioPdf_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/scenarios/1/pdf"))
                .andExpect(status().isNotFound());
    }
}