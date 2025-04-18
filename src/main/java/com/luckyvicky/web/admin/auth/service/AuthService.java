package com.luckyvicky.web.admin.auth.service;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.admin.auth.repository.AuthRepository;
import com.luckyvicky.web.common.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    public ApiResponse<?> login(MemberDTO memberDTO) {

        return new ApiResponse<>(null);

    }


}
