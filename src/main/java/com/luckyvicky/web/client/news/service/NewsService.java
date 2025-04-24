package com.luckyvicky.web.client.news.service;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.common.util.EncodeUtil;
import com.luckyvicky.common.util.FileUtil;
import com.luckyvicky.common.util.PageUtil;
import com.luckyvicky.common.vo.PageVO;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.dto.NewsSearchDTO;
import com.luckyvicky.web.client.news.entity.News;
import com.luckyvicky.web.client.news.entity.NewsFile;
import com.luckyvicky.web.client.news.enums.NewsCategoryEnum;
import com.luckyvicky.web.client.news.repository.NewsFileRepository;
import com.luckyvicky.web.client.news.repository.NewsRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.luckyvicky.web.client.news.entity.QNews.news;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsFileRepository newsFileRepository;

    private final JPAQueryFactory jpaQueryFactory;

    private final EncodeUtil encodeUtil;
    private final FileUtil fileUtil;
    private final PageUtil pageUtil;

    @Transactional
    public Long save(NewsDTO newsDTO) {

        try {

            // News 등록
            News news = News.builder()
                    .category(NewsCategoryEnum.valueOf(newsDTO.getCategory().toUpperCase()))
                    .title(newsDTO.getTitle())
                    .content(newsDTO.getContent())
                    .nickname(newsDTO.getNickname())
                    .password(encodeUtil.encode(newsDTO.getPassword()))
                    .build();

            newsRepository.save(news);

            // NewsFile 등록
            if(newsDTO.getNewsFile() != null && !newsDTO.getNewsFile().isEmpty()) {

                MultipartFile file = newsDTO.getNewsFile();

                // 업로드
                Map<String, String> fileInfoMap = fileUtil.upload(file);

                NewsFile newsFile = NewsFile.builder()
                        .news(news)
                        .originalName(fileInfoMap.get("originalName"))
                        .extension(fileInfoMap.get("extension"))
                        .saveName(fileInfoMap.get("saveName"))
                        .savePath(fileInfoMap.get("savePath"))
                        .size(Long.valueOf(fileInfoMap.get("size")))
                        .build();

                newsFileRepository.save(newsFile);

            }

            return news.getId();

        } catch (Exception e) {
            throw new RuntimeException("News 등록 실패 :: NewsService.save()", e);
        }

    }


    public ApiResponse<List<NewsDTO>> findPage(NewsSearchDTO newsSearchDTO) {

        try {

            //! TODO (삭제 데이터 고려)
            Long totalCount = jpaQueryFactory
                    .select(news.count())
                    .from(news)
                    .fetchOne();

            newsSearchDTO.setTotalCount(totalCount);

            // 페이징 정보
            PageVO pageVO = pageUtil.getPageVO(newsSearchDTO);

            // 페이징 반환
            List<News> newsList = jpaQueryFactory
                    .select(news)
                    .from(news)
                    .limit(pageVO.getLimit())
                    .offset(pageVO.getStartIndex())
                    .fetch();

            List<NewsDTO> newsDTOList = newsList.stream()
                    .map(news -> news.toDTO(news))
                    .collect(Collectors.toList());

            HashMap<String, Object> metaData = new HashMap<>();
            metaData.put("totalCount", totalCount);

            return new ApiResponse(newsDTOList, metaData);

        } catch (Exception e) {
            throw new RuntimeException("News 조회 실패 :: NewsService.findPage()", e);
        }

    }

}
