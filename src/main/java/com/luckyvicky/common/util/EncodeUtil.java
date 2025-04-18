package com.luckyvicky.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncodeUtil {

    // BCryptPasswordEncoder 인스턴스
    private final PasswordEncoder passwordEncoder;

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

}
