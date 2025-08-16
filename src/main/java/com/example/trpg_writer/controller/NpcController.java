package com.example.trpg_writer.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.trpg_writer.dto.NpcDetailDto;
import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.form.NpcForm;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.BootyService;
import com.example.trpg_writer.service.NpcService;
import com.example.trpg_writer.service.PartService;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SkillService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}")
@RequiredArgsConstructor
public final class NpcController {

    private final NpcService npcService;
    private final ScenarioService scenarioService;
    private final PartService partService;
    private final BootyService bootyService;
    private final SkillService skillService;

    // --- HTML Page Rendering ---

    @GetMapping("/npcs/create")
    public String createPage(@PathVariable("scenarioId") Integer scenarioId,
                             @RequestParam(name = "sceneId", required = false) Integer sceneId,
                             Model model,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);

        NpcForm npcForm = new NpcForm();
        npcForm.setScenarioId(scenarioId);
        npcForm.setSceneId(sceneId);
        model.addAttribute("npcForm", npcForm);
        model.addAttribute("sceneId", sceneId);
        model.addAttribute("scenarioId", scenarioId);

        model.addAttribute("allParts", partService.findByScenarioId(scenarioId));
        model.addAttribute("allSkills", skillService.findByScenarioId(scenarioId));
        model.addAttribute("allBootys", bootyService.findByScenarioId(scenarioId));

        return "npcs/create";
    }

    @GetMapping("/npcs/{npcId}/edit")
    public String editPage(@PathVariable("scenarioId") Integer scenarioId,
                           @PathVariable("npcId") Integer npcId,
                           @RequestParam(name = "sceneId", required = false) Integer sceneId,
                           Model model,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Npc npc = npcService.findById(npcId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NPC not found"));
        
        NpcForm npcForm = npcService.convertEntityToForm(npc);
        npcForm.setSceneId(sceneId);

        model.addAttribute("npcForm", npcForm);
        model.addAttribute("scenarioId", scenarioId);
        model.addAttribute("sceneId", sceneId);
        model.addAttribute("npcId", npcId);
        
        model.addAttribute("allParts", partService.findByScenarioId(scenarioId));
        model.addAttribute("allSkills", skillService.findByScenarioId(scenarioId));
        model.addAttribute("allBootys", bootyService.findByScenarioId(scenarioId));

        return "npcs/edit";
    }

    // --- API Endpoints ---

    @PostMapping("/api/npcs")
    public ResponseEntity<?> createNpc(@PathVariable("scenarioId") Integer scenarioId,
                                       @RequestBody @Validated NpcForm npcForm,
                                       BindingResult bindingResult,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        npcForm.setScenarioId(scenarioId);
        Npc createdNpc = npcService.create(npcForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNpc);
    }

    @PutMapping("/api/npcs/{npcId}")
    public ResponseEntity<?> updateNpc(@PathVariable("scenarioId") Integer scenarioId,
                                       @PathVariable("npcId") Integer npcId,
                                       @RequestBody @Validated NpcForm npcForm,
                                       BindingResult bindingResult,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        npcForm.setScenarioId(scenarioId);
        npcForm.setId(npcId);
        Npc updatedNpc = npcService.update(npcForm);
        return ResponseEntity.ok(updatedNpc);
    }

    @GetMapping("/npcs/{npcId}/details")
    @ResponseBody
    public NpcDetailDto getNpcDetails(@PathVariable("scenarioId") Integer scenarioId,
                                      @PathVariable("npcId") Integer npcId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Npc npc = npcService.findById(npcId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NPC not found"));
        return npcService.convertToDetailDto(npc);
    }

    @DeleteMapping("/npcs/{npcId}")
    public ResponseEntity<Void> delete(@PathVariable("scenarioId") Integer scenarioId,
                                     @PathVariable("npcId") Integer npcId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        npcService.delete(npcId);
        return ResponseEntity.ok().build();
    }

    
}
