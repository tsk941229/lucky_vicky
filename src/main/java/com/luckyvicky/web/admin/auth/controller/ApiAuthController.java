package com.luckyvicky.web.admin.auth.controller;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.admin.auth.service.AuthService;
import com.luckyvicky.web.common.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiAuthController {

    private final AuthService authService;

    @PostMapping("/api/admin/auth/login")
    public ApiResponse<?> login(@RequestBody MemberDTO memberDTO) {
        ApiResponse<?> apiResponse = authService.login(memberDTO);
        return apiResponse;
    }

}
