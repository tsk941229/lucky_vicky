package com.luckyvicky.common.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageVO {

    /*
        페이징 공식

        page		// 현재 페이지
        size		// 페이지 크기 (start~end)
        limit		// 페이지당 보여줄 아이템 수
        totalCount	// 총 아이템 수

        totalPages	// 총 페이지 (((totalCount - 1) / limit) + 1)
        startIndex	// 시작번호 (page - 1) * limit; // offset
        endIndex	// 끝번호 Math.min(startIndex + limit - 1, totalCount - 1)

        // 페이지 버튼용
        startPage   // ((page - 1) / size) * size + 1
        endPage     // Math.min(startPage + size - 1, totalPages)
    */

    private Integer page;
    private Integer size;
    private Integer limit;
    private Long totalCount;

    private Integer totalPages;
    private Integer startIndex;
    private Integer endIndex;

    private Integer startPage;
    private Integer endPage;


}
