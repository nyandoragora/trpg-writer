package com.example.trpg_writer.controller;

import com.example.trpg_writer.entity.Part;
import com.example.trpg_writer.form.PartForm;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.PartService;
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
@RequestMapping("/api/parts")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated PartForm partForm, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Part newPart = partService.create(partForm, userDetails.getUser());
        return ResponseEntity.ok(newPart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        partService.delete(id, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
