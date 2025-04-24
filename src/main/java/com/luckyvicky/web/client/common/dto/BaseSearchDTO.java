package com.luckyvicky.web.client.common.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseSearchDTO {

    private String keyword;

    // 페이징
    private Integer page;
    private Integer totalCount;

}
