package com.luckyvicky.web.client.news.controller;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiNewsController {

    private final NewsService newsService;

    @PostMapping("/api/client/news/save")
    public ApiResponse<Long> save(NewsDTO newsDTO) {
        Long id = newsService.save(newsDTO);
        return new ApiResponse<>(id);
    }

}
