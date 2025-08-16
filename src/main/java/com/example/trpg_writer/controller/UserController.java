package com.example.trpg_writer.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.ScenarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public final class UserController {
    private final ScenarioService scenarioService;

    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable, Model model) {

        User user = userDetailsImpl.getUser();
        Page<Scenario> scenarios = scenarioService.findByUser(user, pageable);

        model.addAttribute("user", user);
        model.addAttribute("scenarios", scenarios);

        return "users/index";
    }
}