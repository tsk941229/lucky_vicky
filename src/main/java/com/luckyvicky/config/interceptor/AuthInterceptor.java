package com.luckyvicky.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 세션 있으면 반환, 없으면 null
        HttpSession session = request.getSession(false);

        boolean isLogin = (session != null && session.getAttribute("userInfo") != null);

        if(!isLogin) {
            response.sendRedirect("/admin/auth/login");
            return false;
        }

        return true;
    }
}
