package com.luckyvicky.web.client.news.service;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.common.util.EncodeUtil;
import com.luckyvicky.common.util.FileUtil;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.entity.News;
import com.luckyvicky.web.client.news.entity.NewsFile;
import com.luckyvicky.web.client.news.enums.NewsCategoryEnum;
import com.luckyvicky.web.client.news.repository.NewsFileRepository;
import com.luckyvicky.web.client.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsFileRepository newsFileRepository;
    private final EncodeUtil encodeUtil;
    private final FileUtil fileUtil;

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
}
