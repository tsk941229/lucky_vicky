package com.luckyvicky.web.client.news.enums;


public enum NewsCategoryEnum {
    NORMAL("일반"), // 일반
    NOTICE("공지사항");  // 공지사항

    private String value;

    NewsCategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
