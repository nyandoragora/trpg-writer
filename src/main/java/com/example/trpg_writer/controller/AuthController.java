package com.example.trpg_writer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.trpg_writer.service.UserService;
import com.example.trpg_writer.form.SignupForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(@ModelAttribute SignupForm signupForm) {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String register(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        if (userService.isEmailRegistered(signupForm.getEmail())) {
            bindingResult.rejectValue("email", "error.signupForm", "このメールアドレスは既に登録されています。");
            return "auth/signup";
        }

        if (!signupForm.getPassword().equals(signupForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "error.signupForm", "パスワードとパスワード（確認用）が一致しません。");
            return "auth/signup";
        }

        userService.create(signupForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました。");

        return "redirect:/login";
    }
}
