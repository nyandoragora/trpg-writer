package com.example.trpg_writer.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/scenarios")
@RequiredArgsConstructor
public class ScenarioController {

    private final ScenarioService scenarioService;
    private final SceneService sceneService;

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, @PageableDefault(page = 0, size = 5, sort = "createdAt") Pageable pageable, Model model) {
        Scenario scenario = scenarioService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        
        Page<Scene> scenes = sceneService.findByScenarioOrderByCreatedAtAsc(scenario, pageable);

        model.addAttribute("scenario", scenario);
        model.addAttribute("scenes", scenes);

        return "scenarios/edit";
    }
}
