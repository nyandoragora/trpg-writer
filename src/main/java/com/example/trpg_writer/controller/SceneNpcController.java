package com.example.trpg_writer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.trpg_writer.service.SceneNpcService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios/{scenarioId}/scenes/{sceneId}")
@RequiredArgsConstructor
public class SceneNpcController {

    private final SceneNpcService sceneNpcService;

    @PostMapping("/npcs/{npcId}/add")
    public ResponseEntity<Void> addNpcToScene(@PathVariable Integer sceneId, @PathVariable Integer npcId) {
        sceneNpcService.addNpcToScene(sceneId, npcId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/scene-npcs/{sceneNpcId}/remove")
    public ResponseEntity<Void> removeNpcFromScene(@PathVariable Long sceneNpcId) {
        sceneNpcService.removeNpcFromScene(sceneNpcId);
        return ResponseEntity.ok().build();
    }
}
