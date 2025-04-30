package com.luckyvicky.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CookieUtil {

    public void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24); // 24시간
        response.addCookie(cookie);
    }

    public List<String> getCookieValues(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();

        if(cookies == null) return null;

        List<String> resultList = new ArrayList<String>();

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(name)) {
                resultList.add(cookie.getValue());
            }
        }

        return resultList;

    }

}
