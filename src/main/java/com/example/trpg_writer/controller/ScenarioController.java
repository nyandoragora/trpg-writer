package com.example.trpg_writer.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.trpg_writer.entity.Info;
import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.entity.SceneInfo;
import com.example.trpg_writer.entity.SceneNpc;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.ScenarioForm;
import com.example.trpg_writer.repository.SceneNpcRepository;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.NpcService;
import com.example.trpg_writer.service.PdfService;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneInfoService;
import com.example.trpg_writer.service.SceneService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios")
@RequiredArgsConstructor
public final class ScenarioController {

    private final ScenarioService scenarioService;
    private final SceneService sceneService;
    private final PdfService pdfService;
    private final TemplateEngine templateEngine;
    private final SceneInfoService sceneInfoService;
    private final NpcService npcService;
    private final SceneNpcRepository sceneNpcRepository;

    @GetMapping("/create")
    public String create(@ModelAttribute ScenarioForm scenarioForm) {
        return "scenarios/create";
    }

    @PostMapping("/create")
    public String store(@ModelAttribute @Validated ScenarioForm scenarioForm,
                        BindingResult bindingResult,
                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "scenarios/create";
        }

        User user = userDetailsImpl.getUser();
        scenarioService.create(scenarioForm, user);

        redirectAttributes.addFlashAttribute("successMessage", "シナリオを登録しました。");

        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id,
                       @PageableDefault(page = 0, size = 5, sort = "createdAt") Pageable pageable,
                       Model model,
                       @AuthenticationPrincipal UserDetailsImpl userDetails) {

        scenarioService.checkScenarioOwnership(id, userDetails);
        Scenario scenario = scenarioService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        
        ScenarioForm scenarioForm = new ScenarioForm();
        scenarioForm.setId(scenario.getId());
        scenarioForm.setTitle(scenario.getTitle());
        scenarioForm.setIntroduction(scenario.getIntroduction());

        Page<Scene> scenes = sceneService.findByScenarioOrderByCreatedAtAsc(scenario, pageable);

        model.addAttribute("scenario", scenario);
        model.addAttribute("scenarioForm", scenarioForm);
        model.addAttribute("scenes", scenes);

        return "scenarios/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Integer id,
                         @ModelAttribute @Validated ScenarioForm scenarioForm,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                         RedirectAttributes redirectAttributes,
                         Model model) {
                            
        scenarioService.checkScenarioOwnership(id, userDetails);

        if (bindingResult.hasErrors()) {
            Scenario scenario = scenarioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
            Page<Scene> scenes = sceneService.findByScenarioOrderByCreatedAtAsc(scenario, Pageable.unpaged()); // Or handle pagination as needed
            
            model.addAttribute("scenario", scenario);
            model.addAttribute("scenes", scenes);
            model.addAttribute("scenarioForm", scenarioForm);
            return "scenarios/edit";
        }

        scenarioForm.setId(id);
        scenarioService.update(scenarioForm);

        redirectAttributes.addFlashAttribute("successMessage", "シナリオを更新しました。");

        return "redirect:/scenarios/" + id + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes redirectAttributes) {
        scenarioService.checkScenarioOwnership(id, userDetails);
        scenarioService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "シナリオを削除しました。");
        return "redirect:/users";
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(id, userDetails);
        Scenario scenario = scenarioService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        
        List<Scene> scenes = sceneService.findByScenarioOrderByCreatedAtAsc(scenario);

        // Fetch scene-specific infos
        Map<Integer, List<Info>> sceneInfosMap = scenes.stream()
            .collect(Collectors.toMap(
                Scene::getId,
                scene -> sceneInfoService.findBySceneId(scene.getId()).stream()
                                        .map(si -> si.getInfo())
                                        .collect(Collectors.toList())
            ));
        
        // Fetch all scene infos for the summary page
        List<SceneInfo> allSceneInfos = sceneInfoService.findByScenarioId(id);

        // Fetch all unique NPCs for the NPC summary page
        List<Npc> allNpcs = npcService.findUniqueNpcsByScenarioId(id);

        // Fetch all scene-npc relations to count NPCs per scene
        List<SceneNpc> allSceneNpcs = sceneNpcRepository.findByScene_ScenarioId(id);
        Map<Integer, Map<Npc, Long>> sceneNpcCountMap = allSceneNpcs.stream()
            .collect(Collectors.groupingBy(
                sceneNpc -> sceneNpc.getScene().getId(),
                Collectors.groupingBy(
                    SceneNpc::getNpc,
                    Collectors.counting()
                )
            ));

        Context context = new Context();
        context.setVariable("scenario", scenario);
        context.setVariable("scenes", scenes);
        context.setVariable("sceneInfosMap", sceneInfosMap);
        context.setVariable("allSceneInfos", allSceneInfos);
        context.setVariable("allNpcs", allNpcs);
        context.setVariable("sceneNpcCountMap", sceneNpcCountMap);

        String html = templateEngine.process("scenarios/pdf-template", context);
        byte[] pdf = pdfService.generatePdfFromHtml(html);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = scenario.getTitle() + ".pdf";
        // Encode filename for non-ASCII characters
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
