package com.luckyvicky.web.client.common.dto;

import com.luckyvicky.common.vo.PageVO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseSearchDTO {

    private String keyword;

    // 페이징 관련
    private Integer page;
    private Integer size;
    private Integer limit;
    private Long totalCount;

}
