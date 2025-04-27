package com.luckyvicky.web.client.news.controller;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.client.news.dto.NewsCommentDTO;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.dto.NewsSearchDTO;
import com.luckyvicky.web.client.news.enums.NewsCategoryEnum;
import com.luckyvicky.web.client.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;


    @GetMapping("/client/news/list")
    public String list() {
        return "/client/news/list";
    }

    @GetMapping("/client/news/list-inner")
    public String listInner(NewsSearchDTO newsSearchDTO, Model model) {

        ApiResponse<List<NewsDTO>> response = newsService.findNewsPage(newsSearchDTO);

        model.addAttribute("newsList", response.getData());
        model.addAttribute("pageVO", response.getMetaData().get("pageVO"));

        return "/client/news/fragments/list-inner";
    }


    @GetMapping("/client/news/form")
    public String form(Model model) {
        model.addAttribute("categoryList", NewsCategoryEnum.values());
        return "/client/news/form";
    }

    @PostMapping("/client/news/save")
    @ResponseBody
    public ApiResponse<Long> save(NewsDTO newsDTO) {
        Long id = newsService.save(newsDTO);
        return new ApiResponse<>(id);
    }


    @GetMapping("/client/news/detail/{id}")
    public String detail(@PathVariable long id, Model model) {

        ApiResponse<NewsDTO> response = newsService.findNews(id);

        model.addAttribute("newsDTO", response.getData());

        return "/client/news/detail";
    }


    @PostMapping("/client/news/delete")
    @ResponseBody
    public ApiResponse<Boolean> delete(NewsDTO newsDTO) {
        ApiResponse<Boolean> response = newsService.delete(newsDTO);
        return response;
    }


    // news 조회해서 password 매칭만 함
    @PostMapping("/client/news/match-password")
    @ResponseBody
    public ApiResponse<Boolean> matchPassword(NewsDTO newsDTO) {
        ApiResponse<Boolean> response = newsService.matchPassword(newsDTO);
        return response;
    }


    @GetMapping("/client/news/update/{id}")
    public String update(@PathVariable long id, Model model) {
        // 뉴스만 따로 가져오는 것도 고려해보자 (아래 메서드는 댓글도 다 가져오는데, 수정페이지에선 댓글 필요 없음)
        ApiResponse<NewsDTO> response = newsService.findNews(id);

        model.addAttribute("categoryList", NewsCategoryEnum.values());
        model.addAttribute("newsDTO", response.getData());
        return "/client/news/update";
    }

}
