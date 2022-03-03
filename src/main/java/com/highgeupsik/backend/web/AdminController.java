package com.highgeupsik.backend.web;

import com.highgeupsik.backend.dto.UserConfirmDTO;
import com.highgeupsik.backend.service.UserConfirmService;
import com.highgeupsik.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UserService userService;
    private final UserConfirmService userConfirmService;

    @GetMapping("/users")
    public String userConfirms(Model model,
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        Page<UserConfirmDTO> userConfirms = userConfirmService.findAll(pageNum);
        model.addAttribute("userConfirms", userConfirms.getContent());
        return "home"; //테스트
    }

    @PostMapping("/users/{userId}/authorize") //수락
    public String userAccept(@PathVariable Long userId) {
        userService.acceptUser(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}") //거부
    public String userReject(@PathVariable Long userId) {
        userService.rejectUser(userId);
        return "redirect:/admin/users";
    }
}
