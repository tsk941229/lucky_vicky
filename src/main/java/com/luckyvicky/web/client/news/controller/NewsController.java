package com.luckyvicky.web.client.news.controller;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.client.news.dto.NewsCommentDTO;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.dto.NewsSearchDTO;
import com.luckyvicky.web.client.news.enums.NewsCategoryEnum;
import com.luckyvicky.web.client.news.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;


    @GetMapping("/client/news/list")
    public String list() {
        return "client/news/list";
    }

    @GetMapping("/client/news/list-inner")
    public String listInner(NewsSearchDTO newsSearchDTO, Model model) {

        ApiResponse<List<NewsDTO>> response = newsService.findNewsPage(newsSearchDTO);

        model.addAttribute("newsList", response.getData());
        model.addAttribute("pageVO", response.getMetaData().get("pageVO"));

        return "client/news/fragments/list-inner";
    }


    @GetMapping("/client/news/form")
    public String form(@RequestParam(required = false) Long parentId,
                       @RequestParam(required = false) Integer depth,
                       Model model) {

        // 답글일 때
        if(parentId != null && depth != null) {
            Map<String, Object> parentInfo = new HashMap<>();
            parentInfo.put("parentId", parentId);
            parentInfo.put("depth", depth);
            model.addAttribute("parentInfo", parentInfo);
        }

        model.addAttribute("categoryList", NewsCategoryEnum.values());

        return "client/news/form";
    }

    @PostMapping("/client/news/save")
    @ResponseBody
    public ApiResponse<Long> save(NewsDTO newsDTO) {
        Long id = newsService.save(newsDTO);
        return new ApiResponse<>(id);
    }


    @GetMapping("/client/news/detail/{id}")
    public String detail(@PathVariable long id, HttpServletResponse response, HttpServletRequest request, Model model) {

        ApiResponse<NewsDTO> apiResponse = newsService.findNewsForDetail(id, response, request);

        model.addAttribute("newsDTO", apiResponse.getData());

        return "client/news/detail";
    }

    // 좋아요 체크
    @GetMapping("/client/news/check-likes/{id}")
    @ResponseBody
    public ApiResponse<Boolean> checkLikes(@PathVariable long id, HttpServletRequest request) {

        boolean isLiked = newsService.checkLikes(id, request);

        return new ApiResponse<>(isLiked);

    }

    @PostMapping("/client/news/toggle-likes")
    @ResponseBody
    public ApiResponse<Integer> toggleLikes(@RequestParam long id,
                                      @RequestParam boolean isUp,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        Integer likesCount = newsService.toggleLikes(id, isUp, request, response);

        return new ApiResponse<>(likesCount);
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
        ApiResponse<NewsDTO> response = newsService.findNewsForUpdate(id);

        model.addAttribute("categoryList", NewsCategoryEnum.values());
        model.addAttribute("newsDTO", response.getData());
        return "client/news/update";
    }

    @PostMapping("/client/news/update")
    @ResponseBody
    public ApiResponse<?> update(NewsDTO newsDTO) {
        Long id = newsService.update(newsDTO);
        ApiResponse<?> response = new ApiResponse<>(id);
        return response;
    }

}
