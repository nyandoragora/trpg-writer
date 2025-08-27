package com.example.trpg_writer.controller;

import com.example.trpg_writer.dto.InfoWithScenesDto;
import com.example.trpg_writer.dto.SceneEditPageData;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.form.SceneForm;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneDataService;
import com.example.trpg_writer.service.SceneService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/scenarios/{scenarioId}/scenes")
@RequiredArgsConstructor
public final class SceneController {

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

    @DeleteMapping("/{sceneId}")
    public ResponseEntity<?> deleteScene(@PathVariable("scenarioId") Integer scenarioId,
                                         @PathVariable("sceneId") Integer sceneId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        sceneService.delete(sceneId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sceneId}/uploadImage")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@PathVariable("scenarioId") Integer scenarioId,
                                         @PathVariable("sceneId") Integer sceneId,
                                         @RequestParam("imageFile") MultipartFile imageFile,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        try {
            String imageUrl = sceneService.saveImage(sceneId, imageFile);
            if (imageUrl != null) {
                return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "画像ファイルが空か、無効です。"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "画像のアップロードに失敗しました: " + e.getMessage()));
        }
    }

    @GetMapping("/all-infos")
    public ResponseEntity<List<InfoWithScenesDto>> getInfosWithScenes(
            @PathVariable Integer scenarioId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        List<InfoWithScenesDto> data = sceneDataService.getInfosWithScenes(scenarioId);
        return ResponseEntity.ok(data);
    }
}
