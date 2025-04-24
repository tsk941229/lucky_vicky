package com.luckyvicky.web.client.news.controller;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.dto.NewsSearchDTO;
import com.luckyvicky.web.client.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApiNewsController {

    private final NewsService newsService;

    @PostMapping("/api/client/news/save")
    @ResponseBody
    public ApiResponse<Long> save(NewsDTO newsDTO) {
        Long id = newsService.save(newsDTO);
        return new ApiResponse<>(id);
    }

    @GetMapping("/api/client/news/list")
    public String list(NewsSearchDTO newsSearchDTO, Model model) {

        ApiResponse<List<NewsDTO>> response = newsService.findPage(newsSearchDTO);

        model.addAttribute("newsList", response.getData());
        model.addAttribute("totalCount", response.getMetaData().get("totalCount"));

        return "/client/news/fragments/list-inner";
    }

}
