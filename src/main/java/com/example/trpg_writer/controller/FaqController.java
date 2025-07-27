package com.example.trpg_writer.controller;

import com.example.trpg_writer.entity.Faq;
import com.example.trpg_writer.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    public String index(Model model) {
        List<Faq> faqs = faqService.findAll();
        model.addAttribute("faqs", faqs);
        return "faqs/index";
    }
}
