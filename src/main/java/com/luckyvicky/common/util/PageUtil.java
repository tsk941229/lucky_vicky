package com.luckyvicky.common.util;

import com.luckyvicky.common.vo.PageVO;
import com.luckyvicky.web.client.common.dto.BaseSearchDTO;
import org.springframework.stereotype.Component;

@Component
public class PageUtil {


    // 파라미터 다형성
    public PageVO getPageVO(BaseSearchDTO searchDTO) {

        // 페이징 값 초기화
        int page = searchDTO.getPage();
        int size = searchDTO.getSize();
        int limit = searchDTO.getLimit();
        long totalCount = searchDTO.getTotalCount();

        int totalPages = (int) (((totalCount - 1) / limit) + 1);
        int startIndex = (page - 1) * size;
        int endIndex = (int) Math.min(startIndex + limit - 1, totalCount - 1);

        return PageVO.builder()
                .page(page)
                .size(size)
                .limit(limit)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .startIndex(startIndex)
                .endIndex(endIndex)
                .build();
    }

}
