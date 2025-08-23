package com.example.trpg_writer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.trpg_writer.entity.Info;
import com.example.trpg_writer.form.InfoForm;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.InfoService;
import com.example.trpg_writer.service.ScenarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/scenarios/{scenarioId}/infos")
@RequiredArgsConstructor
public final class InfoController {

    private final InfoService infoService;
    private final ScenarioService scenarioService;

    // 情報の保存
    @PostMapping
    public ResponseEntity<?> store(@PathVariable("scenarioId") Integer scenarioId,
                                   @RequestBody @Validated InfoForm infoForm,
                                   BindingResult bindingResult,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        infoForm.setScenarioId(scenarioId);
        infoService.create(infoForm);

        return ResponseEntity.ok().build();
    }

    // 情報の更新
    @PutMapping("/{infoId}")
    public ResponseEntity<?> update(@PathVariable("scenarioId") Integer scenarioId,
                                    @PathVariable("infoId") Integer infoId,
                                    @RequestBody @Validated InfoForm infoForm,
                                    BindingResult bindingResult,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Info info = infoService.findById(infoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Info not found"));

        if (!info.getScenario().getId().equals(scenarioId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Info does not belong to the specified scenario");
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        infoForm.setScenarioId(scenarioId);
        infoService.update(info, infoForm);

        return ResponseEntity.ok().build();
    }

        // 情報の削除
    @DeleteMapping("/{infoId}")
    public ResponseEntity<?> delete(@PathVariable("scenarioId") Integer scenarioId,
                                    @PathVariable("infoId") Integer infoId,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Info info = infoService.findById(infoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Info not found"));

        if (!info.getScenario().getId().equals(scenarioId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Info does not belong to the specified scenario");
        }

        infoService.delete(infoId);

        return ResponseEntity.ok().build();
    }

    // 情報の詳細を取得
    @GetMapping("/{infoId}")
    public ResponseEntity<Info> getInfoDetails(@PathVariable("scenarioId") Integer scenarioId,
                                               @PathVariable("infoId") Integer infoId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        Info info = infoService.findById(infoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Info not found"));

        if (!info.getScenario().getId().equals(scenarioId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Info does not belong to the specified scenario");
        }

        return ResponseEntity.ok(info);
    }
}
