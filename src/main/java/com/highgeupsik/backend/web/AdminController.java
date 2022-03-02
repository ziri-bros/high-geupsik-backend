package com.highgeupsik.backend.web;

import com.highgeupsik.backend.dto.UserConfirmDTO;
import com.highgeupsik.backend.service.UserConfirmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UserConfirmService userConfirmService;

    @ResponseBody
    @GetMapping("/users")
    public String userConfirms(Model model,
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        Page<UserConfirmDTO> userConfirms = userConfirmService.findAll(pageNum);
        model.addAttribute("users", userConfirms.getContent());
        return "OK"; //테스트
    }
}
