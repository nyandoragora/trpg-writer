package com.example.trpg_writer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneService;
import com.example.trpg_writer.service.NpcService;
import com.example.trpg_writer.service.InfoService;
import com.example.trpg_writer.service.PartService;
import com.example.trpg_writer.service.BootyService;
import com.example.trpg_writer.service.SkillService;
import com.example.trpg_writer.service.SceneNpcService;
import com.example.trpg_writer.service.SceneInfoService;
import com.example.trpg_writer.service.NpcPartService;
import com.example.trpg_writer.service.NpcSkillService;
import com.example.trpg_writer.service.NpcBootyService;

import lombok.RequiredArgsConstructor;

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

    @GetMapping("/{sceneId}/edit")
    public String edit(@PathVariable("scenarioId") Integer scenarioId,
                       @PathVariable("sceneId") Integer sceneId,
                       Model model) {

        Scenario scenario = scenarioService.findById(scenarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));

        Scene scene = sceneService.findById(sceneId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scene not found"));

        if (isNotBelongToScenario(scene, scenario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scene does not belong to the specified scenario");
        }

        // シナリオに紐づく全NPC、情報、パーツ、戦利品、スキルを取得
        model.addAttribute("allNpcs", npcService.findByScenarioId(scenarioId));
        model.addAttribute("allInfos", infoService.findByScenarioId(scenarioId));
        model.addAttribute("allParts", partService.findByScenarioId(scenarioId));
        model.addAttribute("allBootys", bootyService.findByScenarioId(scenarioId));
        model.addAttribute("allSkills", skillService.findByScenarioId(scenarioId));

        // シーンに紐づくNPCと情報を取得
        model.addAttribute("sceneNpcs", sceneNpcService.findBySceneId(sceneId));
        model.addAttribute("sceneInfos", sceneInfoService.findBySceneId(sceneId));

        // シナリオに紐づくNPCのパーツ、スキル、戦利品を取得
        model.addAttribute("npcParts", npcPartService.findByNpcScenarioId(scenarioId));
        model.addAttribute("npcSkills", npcSkillService.findByNpcScenarioId(scenarioId));
        model.addAttribute("npcBootys", npcBootyService.findByNpcScenarioId(scenarioId));


        model.addAttribute("scenario", scenario);
        model.addAttribute("scene", scene);

        return "scenarios/scenes/edit";
    }

    // シーンが指定されたシナリオに属さない、想定外なものでないかチェック
    private boolean isNotBelongToScenario(Scene scene, Scenario scenario) {
        return !scene.getScenario().getId().equals(scenario.getId());
    }
}
