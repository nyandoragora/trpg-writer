package com.example.trpg_writer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.trpg_writer.dto.SceneEditPageData;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.form.SceneForm;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneDataService;
import com.example.trpg_writer.service.SceneService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}/scenes")
@RequiredArgsConstructor
public class SceneController {

    private final ScenarioService scenarioService;
    private final SceneService sceneService;
    private final SceneDataService sceneDataService;

    @Value("${tinymce.api-key}")
    private String tinymceApiKey;

    @GetMapping("/create")
    public String create(@PathVariable("scenarioId") Integer scenarioId, Model model, @ModelAttribute SceneForm sceneForm, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Scenario scenario = scenarioService.findById(scenarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        model.addAttribute("scenario", scenario);
        model.addAttribute("scenarioId", scenarioId);
        sceneForm.setGmInfo("このシーンの重要な点をプレビューに表示できます。");
        return "scenarios/scenes/create";
    }

    @PostMapping("/create")
    public String store(@PathVariable("scenarioId") Integer scenarioId,
                        @ModelAttribute @Validated SceneForm sceneForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model,
                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Scenario scenario = scenarioService.findById(scenarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("scenario", scenario);
            model.addAttribute("scenarioId", scenarioId);
            return "scenarios/scenes/create";
        }

        Scene newScene = sceneService.create(sceneForm, scenarioId);

        redirectAttributes.addFlashAttribute("successMessage", "シーンを作成しました。");
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + newScene.getId() + "/edit";
    }

    @GetMapping("/{sceneId}/edit")
    public String edit(@PathVariable("scenarioId") Integer scenarioId,
                       @PathVariable("sceneId") Integer sceneId,
                       Model model,
                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        model.addAttribute("scenarioId", scenarioId);
        model.addAttribute("sceneId", sceneId);
        model.addAttribute("tinymceApiKey", tinymceApiKey);
        return "scenarios/scenes/edit";
    }

    @GetMapping("/{sceneId}/edit-data")
    public ResponseEntity<SceneEditPageData> getEditPageData(
            @PathVariable Integer scenarioId,
            @PathVariable Integer sceneId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        SceneEditPageData data = sceneDataService.getSceneEditPageData(scenarioId, sceneId);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/{sceneId}/update")
    public ResponseEntity<?> update(@PathVariable("scenarioId") Integer scenarioId,
                                     @PathVariable("sceneId") Integer sceneId,
                                     @RequestBody @Validated SceneForm sceneForm,
                                     BindingResult bindingResult,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);

        if (bindingResult.hasErrors()) {
            // エラー内容をJSONで返す
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        sceneService.update(sceneForm, sceneId, scenarioId);

        return ResponseEntity.ok().build(); // 成功したことを示す
    }

    @PostMapping("/{sceneId}/uploadImage")
    public String uploadImage(@PathVariable("scenarioId") Integer scenarioId,
                              @PathVariable("sceneId") Integer sceneId,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        try {
            sceneService.saveImage(sceneId, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "背景画像をアップロードしました。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "画像のアップロードに失敗しました: " + e.getMessage());
        }
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit";
    }
}