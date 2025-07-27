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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.form.NpcForm;
import com.example.trpg_writer.form.SceneForm; // SceneFormをインポート
import com.example.trpg_writer.service.NpcService;
import com.example.trpg_writer.service.ScenarioService; // ScenarioServiceをインポート
import com.example.trpg_writer.service.SceneService; // SceneServiceをインポート
import com.example.trpg_writer.service.InfoService; // InfoServiceをインポート
import com.example.trpg_writer.service.PartService; // PartServiceをインポート
import com.example.trpg_writer.service.RoleService; // RoleServiceをインポート
import com.example.trpg_writer.service.BootyService; // BootyServiceをインポート
import com.example.trpg_writer.service.SkillService; // SkillServiceをインポート
import com.example.trpg_writer.service.SceneNpcService; // SceneNpcServiceをインポート
import com.example.trpg_writer.service.SceneInfoService; // SceneInfoServiceをインポート
import com.example.trpg_writer.service.NpcPartService; // NpcPartServiceをインポート
import com.example.trpg_writer.service.NpcSkillService; // NpcSkillServiceをインポート
import com.example.trpg_writer.service.NpcBootyService; // NpcBootyServiceをインポート

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}/scenes/{sceneId}/npcs")
@RequiredArgsConstructor
public class NpcController {

    private final NpcService npcService;
    private final ScenarioService scenarioService;
    private final SceneService sceneService;
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

    @GetMapping("/create")
    public String create(@PathVariable("scenarioId") Integer scenarioId,
                         @PathVariable("sceneId") Integer sceneId,
                         Model model) {
        NpcForm npcForm = new NpcForm();
        npcForm.setScenarioId(scenarioId);
        model.addAttribute("npcForm", npcForm);
        model.addAttribute("sceneId", sceneId);
        // このGETリクエストは、モーダルを開くためのもので、直接テンプレートをレンダリングするわけではないため、
        // ここで全てのModel属性を設定する必要はない。
        // ただし、Thymeleafのパス解決のために、edit.htmlを返すようにする。
        // 実際には、edit.htmlが読み込まれた際に、npcFormがModelに存在すればモーダルが開く。
        return "scenarios/scenes/edit";
    }

    @PostMapping("/create")
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
            // エラー時はシーン編集画面に直接戻る
            model.addAttribute("scenario", scenario);
            model.addAttribute("scene", scene);
            
            // SceneFormをModelに追加 (SceneController.editからコピー)
            SceneForm sceneForm = new SceneForm();
            sceneForm.setId(scene.getId());
            sceneForm.setTitle(scene.getTitle());
            sceneForm.setContent(scene.getContent());
            sceneForm.setGmInfo(scene.getGmInfo());
            sceneForm.setExistingImagePath(scene.getImagePath());
            model.addAttribute("sceneForm", sceneForm);

            // SceneController.editが追加する他の全ての属性を追加
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
            model.addAttribute("allSceneInfos", infoService.findByScenarioId(scenarioId)); // ここを修正
            model.addAttribute("npcParts", npcPartService.findByNpcScenarioId(scenarioId));
            model.addAttribute("npcSkills", npcSkillService.findByNpcScenarioId(scenarioId));
            model.addAttribute("npcBootys", npcBootyService.findByNpcScenarioId(scenarioId));
            model.addAttribute("allScenes", sceneService.findByScenario(scenario));
            model.addAttribute("gmRoleId", roleService.findByRole("ROLE_GM").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GM Role not found")).getId());
            model.addAttribute("plRoleId", roleService.findByRole("ROLE_PL").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PL Role not found")).getId());

            // エラー情報を含むnpcFormをModelに追加
            model.addAttribute("npcForm", npcForm);
            return "scenarios/scenes/edit";
        }

        npcForm.setScenarioId(scenarioId);
        Npc newNpc = npcService.create(npcForm);

        redirectAttributes.addFlashAttribute("successMessage", "NPCを登録しました。");
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit";
    }
}
