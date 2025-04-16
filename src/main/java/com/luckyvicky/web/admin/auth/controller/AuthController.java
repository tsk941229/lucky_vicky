package com.luckyvicky.web.admin.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/admin/auth/login")
    public String login() {
        return "admin/auth/login";
    }

}
