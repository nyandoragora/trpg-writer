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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.trpg_writer.entity.Info;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.form.InfoForm;
import com.example.trpg_writer.service.InfoService;
import com.example.trpg_writer.service.ScenarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}/infos")
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;
    private final ScenarioService scenarioService;

    // 情報作成フォームの表示
    @GetMapping("/create")
    public String create(@PathVariable("scenarioId") Integer scenarioId, Model model, @ModelAttribute InfoForm infoForm) {
        Scenario scenario = scenarioService.findById(scenarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        model.addAttribute("scenario", scenario);
        return "scenarios/infos/create"; // 仮のテンプレートパス
    }

    // 情報の保存
    @PostMapping("/store")
    public String store(@PathVariable("scenarioId") Integer scenarioId,
                        @RequestParam("sceneId") Integer sceneId, // Add sceneId
                        @ModelAttribute @Validated InfoForm infoForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        Scenario scenario = scenarioService.findById(scenarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("scenario", scenario);
            return "scenarios/infos/create"; // 仮のテンプレートパス
        }

        infoForm.setScenarioId(scenarioId);
        infoForm.setSceneId(sceneId); // Add this line
        infoService.create(infoForm);

        redirectAttributes.addFlashAttribute("successMessage", "情報を登録しました。");
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit"; // Redirect to scene edit page
    }

    // 情報編集フォームの表示
    @GetMapping("/{infoId}/edit")
    public String edit(@PathVariable("scenarioId") Integer scenarioId,
                       @PathVariable("infoId") Integer infoId,
                       Model model) {
        Scenario scenario = scenarioService.findById(scenarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        Info info = infoService.findById(infoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Info not found"));

        if (!info.getScenario().getId().equals(scenarioId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Info does not belong to the specified scenario");
        }

        InfoForm infoForm = new InfoForm();
        infoForm.setId(info.getId());
        infoForm.setScenarioId(scenarioId);
        infoForm.setName(info.getName());
        infoForm.setContent(info.getContent());

        model.addAttribute("scenario", scenario);
        model.addAttribute("info", info);
        model.addAttribute("infoForm", infoForm);

        return "scenarios/infos/edit"; // 仮のテンプレートパス
    }

    // 情報の更新
    @PostMapping("/{infoId}/update")
    public String update(@PathVariable("scenarioId") Integer scenarioId,
                         @RequestParam("sceneId") Integer sceneId, // Add sceneId
                         @PathVariable("infoId") Integer infoId,
                         @ModelAttribute @Validated InfoForm infoForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        Scenario scenario = scenarioService.findById(scenarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        Info info = infoService.findById(infoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Info not found"));

        if (!info.getScenario().getId().equals(scenarioId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Info does not belong to the specified scenario");
        }

        if (bindingResult.hasErrors()) {
            return "scenarios/infos/edit"; 
        }

        infoForm.setScenarioId(scenarioId);
        infoForm.setSceneId(sceneId); // Add this line
        infoService.update(info, infoForm);

        redirectAttributes.addFlashAttribute("successMessage", "情報を更新しました。");
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit"; // Redirect to scene edit page
    }

    // 情報の削除
    @PostMapping("/{infoId}/delete")
    public String delete(@PathVariable("scenarioId") Integer scenarioId,
                         @PathVariable("infoId") Integer infoId,
                         @RequestParam("sceneId") Integer sceneId, // Add sceneId
                         RedirectAttributes redirectAttributes) {
        Info info = infoService.findById(infoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Info not found"));

        if (!info.getScenario().getId().equals(scenarioId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Info does not belong to the specified scenario");
        }

        infoService.delete(infoId);

        redirectAttributes.addFlashAttribute("successMessage", "情報を削除しました。");
        return "redirect:/scenarios/" + scenarioId + "/scenes/" + sceneId + "/edit"; // Redirect to scene edit page
    }
}
