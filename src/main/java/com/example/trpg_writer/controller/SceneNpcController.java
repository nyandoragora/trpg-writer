package com.example.trpg_writer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.SceneNpcService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}/scenes/{sceneId}")
@RequiredArgsConstructor
public final class SceneNpcController {

    private final SceneNpcService sceneNpcService;
    private final ScenarioService scenarioService;

    @PostMapping("/npcs/{npcId}/add")
    public ResponseEntity<Void> addNpcToScene(@PathVariable Integer scenarioId,
                                              @PathVariable Integer sceneId,
                                              @PathVariable Integer npcId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        sceneNpcService.addNpcToScene(sceneId, npcId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/scene-npcs/{sceneNpcId}/remove")
    public ResponseEntity<Void> removeNpcFromScene(@PathVariable Integer scenarioId,
                                                   @PathVariable Long sceneNpcId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
                                                    
        scenarioService.checkScenarioOwnership(scenarioId, userDetails);
        sceneNpcService.removeNpcFromScene(sceneNpcId);
        return ResponseEntity.ok().build();
    }
}
