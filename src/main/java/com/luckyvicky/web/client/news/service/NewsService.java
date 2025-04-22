package com.luckyvicky.web.client.news.service;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    @Transactional
    public ApiResponse<?> save(NewsDTO newsDTO) {

        System.out.println("newsDTO.toString() : " + newsDTO.toString());

        return new ApiResponse<>(false);
    }
}
