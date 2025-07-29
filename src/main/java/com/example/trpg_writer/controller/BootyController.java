package com.example.trpg_writer.controller;

import com.example.trpg_writer.entity.Booty;
import com.example.trpg_writer.form.BootyForm;
import com.example.trpg_writer.service.BootyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bootys")
@RequiredArgsConstructor
public class BootyController {

    private final BootyService bootyService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated BootyForm bootyForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Booty newBooty = bootyService.create(bootyForm);
        return ResponseEntity.ok(newBooty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        bootyService.delete(id);
        return ResponseEntity.ok().build();
    }
}
