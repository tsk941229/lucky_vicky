package com.luckyvicky.common.util;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BreadcrumbUtil {

    private final Map<String, String> breadcrumbMap = new HashMap<>();

    // 스프링이 딱 한번만 생성하게
    public BreadcrumbUtil() {
        // 기본
        breadcrumbMap.put("list", "목록");
        breadcrumbMap.put("detail", "상세");
        breadcrumbMap.put("form", "등록");

        // 페이지
        breadcrumbMap.put("news", "PENTA News");
    }

    public String getBreadcrumb(String uri) {
        return uriToBreadcrumb(uri);
    }

    // 일단 string으로 넘기는데 map으로 넘겨서 화면에서 그리게 수정 할 수도 있음
    private String uriToBreadcrumb(String uri) {

        String[] uriParts = uri.split("/");

        StringBuffer sb = new StringBuffer();

//        if(uriParts.length > 1) {
//            // 기본값
//            sb.append("Home");
//        }

        for (String uriPart : uriParts) {
            String breadcrumbPart = breadcrumbMap.get(uriPart);

            if(StringUtils.isBlank(breadcrumbPart)) continue;

            sb.append(" > ");
            sb.append(breadcrumbPart);

        }

        return sb.toString();
    }

}
