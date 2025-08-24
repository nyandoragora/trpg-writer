package com.example.trpg_writer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneInfoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}/scenes/{sceneId}")
@RequiredArgsConstructor
public final class SceneInfoController {

    private final SceneInfoService sceneInfoService;
    private final ScenarioService scenarioService;

    @PostMapping("/infos/{infoId}/add")
    public ResponseEntity<Void> addInfoToScene(@PathVariable Integer scenarioId,
                                              @PathVariable Integer sceneId,
                                              @PathVariable Integer infoId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        sceneInfoService.addInfoToScene(sceneId, infoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/infos/{infoId}/remove")
    public ResponseEntity<Void> removeInfoFromScene(@PathVariable Integer scenarioId,
                                                   @PathVariable Integer sceneId,
                                                   @PathVariable Integer infoId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
                                                    
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        sceneInfoService.removeInfoFromScene(sceneId, infoId);
        return ResponseEntity.ok().build();
    }
}
