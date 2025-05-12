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

    /*
    * List<String>로 반환
    * 넣을 때도 List<String> 넣기
    * */

    private String separator = "|";

    public void addCookie(HttpServletResponse response, String name, List<String> valueList) {

        String value = String.join(separator, valueList);

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24); // TODO: 자정에 만료되도록 수정
        response.addCookie(cookie);
    }

    public List<String> getCookieValueList(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();

        if(cookies == null) return null;

        Optional<Cookie> optionalCookie = Arrays.stream(cookies).filter(c -> c.getName().equals(name)).findFirst();

        if(optionalCookie.isEmpty()) return null;

        Cookie cookie = optionalCookie.get();

        String value = cookie.getValue();

        // 가변 List로 반환 (length가 1이어도 사용부에서 일관적으로 사용할 수 있게 List로 반환)
        List<String> valueList = new ArrayList<>(List.of(value.split("\\" + separator)));

        return valueList;

    }

    public void removeCookieValue() {

    }

}
