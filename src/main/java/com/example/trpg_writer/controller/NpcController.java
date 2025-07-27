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

import com.example.trpg_writer.form.NpcForm;
import com.example.trpg_writer.service.NpcService;
import com.example.trpg_writer.entity.Npc;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}/scenes/{sceneId}/npcs") // URLパスにsceneIdを追加
@RequiredArgsConstructor
public class NpcController {

    private final NpcService npcService;

    @GetMapping("/create")
    public String create(@PathVariable("scenarioId") Integer scenarioId,
                         @PathVariable("sceneId") Integer sceneId, // sceneIdを追加
                         Model model) {
        NpcForm npcForm = new NpcForm();
        npcForm.setScenarioId(scenarioId); // scenarioIdをNpcFormに設定
        // npcForm.setSceneId(sceneId); // NpcFormにはsceneIdは不要なので設定しない
        model.addAttribute("npcForm", npcForm);
        model.addAttribute("sceneId", sceneId); // リダイレクト用にsceneIdをModelに追加
        return "npcs/create";
    }

    @PostMapping("/create")
    public String store(@PathVariable("scenarioId") Integer scenarioId,
                        @PathVariable("sceneId") Integer sceneId, // sceneIdを追加
                        @ModelAttribute @Validated NpcForm npcForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // エラー時はsceneIdをModelに再設定してフォームに戻る
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.npcForm", bindingResult);
            redirectAttributes.addFlashAttribute("npcForm", npcForm);
            return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/npcs/create";
        }

        npcForm.setScenarioId(scenarioId);
        Npc newNpc = npcService.create(npcForm);

        redirectAttributes.addFlashAttribute("successMessage", "NPCを登録しました。");
        // シーン編集画面にリダイレクト
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit";
    }
}
