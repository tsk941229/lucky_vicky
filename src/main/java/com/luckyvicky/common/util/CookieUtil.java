package com.luckyvicky.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CookieUtil {

    public void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24); // TODO: 자정에 만료되도록 수정
        response.addCookie(cookie);
    }

    public String getCookieValue(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();

        if(cookies == null) return null;

        Optional<Cookie> optionalCookie = Arrays.stream(cookies).filter(c -> c.getName().equals(name)).findFirst();

        if(optionalCookie.isEmpty()) return null;

        Cookie cookie = optionalCookie.get();

        return cookie.getValue();

    }

}
