package com.luckyvicky.common.util;

import com.luckyvicky.common.vo.PageVO;
import com.luckyvicky.web.client.common.dto.BaseSearchDTO;
import org.springframework.stereotype.Component;

@Component
public class PageUtil {


    // 파라미터 다형성
    public PageVO getPageVO(BaseSearchDTO searchDTO, Long totalCount) {

        // 페이징 값 초기화
        int page = searchDTO.getPage();
        int size = searchDTO.getSize();
        int limit = searchDTO.getLimit();

        int totalPages = (int) (((totalCount - 1) / limit) + 1);
        int startIndex = (page - 1) * limit; // offset
        int endIndex = (int) Math.min(startIndex + limit - 1, totalCount - 1);

        // 페이지 버튼 (프론트용)
        int startPage = ((page - 1) / size) * size + 1;
        int endPage = Math.min(startPage + size - 1, totalPages);



        return PageVO.builder()
                .page(page)
                .size(size)
                .limit(limit)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .startIndex(startIndex)
                .endIndex(endIndex)
                .startPage(startPage)
                .endPage(endPage)
                .build();
    }

}
