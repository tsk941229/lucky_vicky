package com.luckyvicky.common.modelAttribute;

import com.luckyvicky.common.util.BreadcrumbUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {

    private final BreadcrumbUtil breadCrumbUtil;

    @ModelAttribute("breadcrumb")
    public String getBreadcrumb(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return breadCrumbUtil.getBreadcrumb(uri);
    }

}
