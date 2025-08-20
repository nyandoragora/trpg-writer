package com.example.trpg_writer.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.UserEditForm;
import com.example.trpg_writer.security.UserDetailsImpl;
import com.example.trpg_writer.service.ScenarioService;
import com.example.trpg_writer.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public final class UserController {
    private final ScenarioService scenarioService;
    private final UserService userService;

    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable, Model model) {

        User user = userDetailsImpl.getUser();
        Page<Scenario> scenarios = scenarioService.findByUser(user, pageable);

        model.addAttribute("user", user);
        model.addAttribute("scenarios", scenarios);

        return "users/index";
    }

    @GetMapping("/my-page")
    public String myPage(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        User user = userDetailsImpl.getUser();
        model.addAttribute("user", user);
        return "users/index";
    }

    @GetMapping("/my-page/edit")
    public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        User user = userDetailsImpl.getUser();
        UserEditForm userEditForm = new UserEditForm(user.getName(), user.getIntroduction());
        
        model.addAttribute("userEditForm", userEditForm);
        
        return "users/edit";
    }

    @PostMapping("/my-page/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm,
                         BindingResult bindingResult,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }

        User user = userDetailsImpl.getUser();
        userService.update(userEditForm, imageFile, user);

        redirectAttributes.addFlashAttribute("successMessage", "プロフィールを更新しました。");

        return "redirect:/users/my-page";
    }

    @PostMapping("/delete")
    public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        User user = userDetailsImpl.getUser();
        userService.delete(user);
        
        // セッションを無効化してログアウトさせる
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
        redirectAttributes.addFlashAttribute("successMessage", "アカウントを削除しました。ご利用いただきありがとうございました。");

        return "redirect:/users/deleted";
    }

    @GetMapping("/deleted")
    public String deleted() {
        return "users/deleted";
    }
}

