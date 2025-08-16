package com.example.trpg_writer.controller;

import com.example.trpg_writer.entity.Skill;
import com.example.trpg_writer.form.SkillForm;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public final class SkillController {

    private final SkillService skillService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated SkillForm skillForm, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Skill newSkill = skillService.create(skillForm, userDetails.getUser());
        return ResponseEntity.ok(newSkill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        skillService.delete(id, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
