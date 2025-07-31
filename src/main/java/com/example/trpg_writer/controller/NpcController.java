package com.example.trpg_writer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.form.NpcForm;
import com.example.trpg_writer.service.NpcService;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneService;
import com.example.trpg_writer.service.PartService;
import com.example.trpg_writer.service.BootyService;
import com.example.trpg_writer.service.SkillService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}")
@RequiredArgsConstructor
public class NpcController {

    private final NpcService npcService;
    private final ScenarioService scenarioService;
    private final SceneService sceneService;
    private final PartService partService;
    private final BootyService bootyService;
    private final SkillService skillService;

    @GetMapping("/scenes/{sceneId}/npcs/create")
    public String create(@PathVariable("scenarioId") Integer scenarioId,
                         @PathVariable("sceneId") Integer sceneId,
                         Model model) {
        NpcForm npcForm = new NpcForm();
        npcForm.setScenarioId(scenarioId);
        model.addAttribute("npcForm", npcForm);
        model.addAttribute("sceneId", sceneId);

        model.addAttribute("allParts", partService.findByScenarioId(scenarioId));
        model.addAttribute("allSkills", skillService.findByScenarioId(scenarioId));
        model.addAttribute("allBootys", bootyService.findByScenarioId(scenarioId));

        return "npcs/create";
    }

    @PostMapping("/scenes/{sceneId}/npcs/create")
    public String store(@PathVariable("scenarioId") Integer scenarioId,
                        @PathVariable("sceneId") Integer sceneId,
                        @ModelAttribute @Validated NpcForm npcForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        Scenario scenario = scenarioService.findById(scenarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        Scene scene = sceneService.findById(sceneId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scene not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("scenario", scenario);
            model.addAttribute("scene", scene);
            model.addAttribute("allParts", partService.findByScenarioId(scenarioId));
            model.addAttribute("allSkills", skillService.findByScenarioId(scenarioId));
            model.addAttribute("allBootys", bootyService.findByScenarioId(scenarioId));
            model.addAttribute("npcForm", npcForm);
            return "npcs/create";
        }

        npcForm.setScenarioId(scenarioId);
        npcService.create(npcForm);

        redirectAttributes.addFlashAttribute("successMessage", "NPCを登録しました。");
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit";
    }

    @GetMapping("/npcs/{npcId}/details")
    @ResponseBody
    public Npc getNpcDetails(@PathVariable("npcId") Integer npcId) {
        return npcService.findById(npcId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NPC not found"));
    }

    @PostMapping("/scenes/{sceneId}/npcs/{npcId}/delete")
    public ResponseEntity<Void> delete(@PathVariable("scenarioId") Integer scenarioId,
                                     @PathVariable("sceneId") Integer sceneId,
                                     @PathVariable("npcId") Integer npcId) {
        npcService.delete(npcId);
        return ResponseEntity.ok().build();
    }
}