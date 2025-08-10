package com.example.trpg_writer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.form.NpcForm;
import com.example.trpg_writer.form.SceneForm;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.BootyService;
import com.example.trpg_writer.service.InfoService;
import com.example.trpg_writer.service.NpcBootyService;
import com.example.trpg_writer.service.NpcPartService;
import com.example.trpg_writer.service.NpcService;
import com.example.trpg_writer.service.NpcSkillService;
import com.example.trpg_writer.service.PartService;
import com.example.trpg_writer.service.RoleService;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneInfoService;
import com.example.trpg_writer.service.SceneNpcService;
import com.example.trpg_writer.service.SceneService;
import com.example.trpg_writer.service.SkillService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/scenarios/{scenarioId}/scenes")
@RequiredArgsConstructor
public class SceneController {

    private final ScenarioService scenarioService;
    private final SceneService sceneService;
    private final NpcService npcService;
    private final InfoService infoService;
    private final PartService partService;
    private final BootyService bootyService;
    private final SkillService skillService;
    private final SceneNpcService sceneNpcService;
    private final SceneInfoService sceneInfoService;
    private final NpcPartService npcPartService;
    private final NpcSkillService npcSkillService;
    private final NpcBootyService npcBootyService;
    private final RoleService roleService;

    @Value("${tinymce.api-key}")
    private String tinymceApiKey;

    @GetMapping("/create")
    public String create(@PathVariable("scenarioId") Integer scenarioId, Model model, @ModelAttribute SceneForm sceneForm, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Scenario scenario = scenarioService.findById(scenarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        model.addAttribute("scenario", scenario);
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
            return "scenarios/scenes/create";
        }

        Scene newScene = sceneService.create(sceneForm, scenario);

        redirectAttributes.addFlashAttribute("successMessage", "シーンを作成しました。");
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + newScene.getId() + "/edit";
    }

    @GetMapping("/{sceneId}/edit")
    public String edit(@PathVariable("scenarioId") Integer scenarioId,
                       @PathVariable("sceneId") Integer sceneId,
                       Model model,
                       @ModelAttribute SceneForm sceneForm,
                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Scenario scenario = scenarioService.findById(scenarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        Scene scene = sceneService.findById(sceneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scene not found"));

        if (!scene.getScenario().getId().equals(scenario.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scene does not belong to the specified scenario");
        }

        // Populate sceneForm only if not already populated by redirect attributes
        if (!model.containsAttribute("sceneForm")) {
            sceneForm.setId(scene.getId());
            sceneForm.setTitle(scene.getTitle());
            sceneForm.setContent(scene.getContent());
            sceneForm.setGmInfo(scene.getGmInfo());
            sceneForm.setExistingImagePath(scene.getImagePath());
            model.addAttribute("sceneForm", sceneForm);
        }


        model.addAttribute("allNpcs", npcService.findByScenarioId(scenarioId));
        model.addAttribute("allInfos", infoService.findByScenarioId(scenarioId));
        List<String> infoNames = sceneInfoService.findBySceneId(sceneId).stream()
                .map(sceneInfo -> sceneInfo.getInfo().getName())
                .collect(Collectors.toList());
        model.addAttribute("infoNames", infoNames);
        model.addAttribute("allParts", partService.findByScenarioId(scenarioId));
        model.addAttribute("allBootys", bootyService.findByScenarioId(scenarioId));
        model.addAttribute("allSkills", skillService.findByScenarioId(scenarioId));
        model.addAttribute("sceneNpcs", sceneNpcService.findBySceneId(sceneId));
        model.addAttribute("sceneInfos", sceneInfoService.findBySceneId(sceneId));
        model.addAttribute("allSceneInfos", sceneInfoService.findByScenarioId(scenarioId));
        model.addAttribute("npcParts", npcPartService.findByNpcScenarioId(scenarioId));
        model.addAttribute("npcSkills", npcSkillService.findByNpcScenarioId(scenarioId));
        model.addAttribute("npcBootys", npcBootyService.findByNpcScenarioId(scenarioId));
        model.addAttribute("allScenes", sceneService.findByScenario(scenario));
        model.addAttribute("scenario", scenario);
        model.addAttribute("scene", scene);
        model.addAttribute("tinymceApiKey", tinymceApiKey);

        NpcForm npcForm = new NpcForm();
        npcForm.setScenarioId(scenarioId);
        model.addAttribute("npcForm", npcForm);

        model.addAttribute("gmRoleId", roleService.findByRole("ROLE_GM")
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GM Role not found")).getId());

        model.addAttribute("plRoleId", roleService.findByRole("ROLE_PL")
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PL Role not found")).getId());

        return "scenarios/scenes/edit";
    }

    @PostMapping("/{sceneId}/update")
    public String update(@PathVariable("scenarioId") Integer scenarioId,
                         @PathVariable("sceneId") Integer sceneId,
                         @ModelAttribute @Validated SceneForm sceneForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.sceneForm", bindingResult);
            redirectAttributes.addFlashAttribute("sceneForm", sceneForm);
            return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit";
        }

        Scene scene = sceneService.findById(sceneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scene not found"));

        scene.setTitle(sceneForm.getTitle());
        scene.setContent(sceneForm.getContent());
        scene.setGmInfo(sceneForm.getGmInfo());

        sceneService.update(scene, sceneForm);

        redirectAttributes.addFlashAttribute("successMessage", "シーンを更新しました。");
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit";
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
