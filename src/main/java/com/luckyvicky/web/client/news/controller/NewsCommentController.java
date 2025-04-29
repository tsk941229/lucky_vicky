package com.luckyvicky.web.client.news.controller;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.client.news.dto.NewsCommentDTO;
import com.luckyvicky.web.client.news.service.NewsCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class NewsCommentController {

    private final NewsCommentService newsCommentService;

    @PostMapping("/client/news/comment/save")
    @ResponseBody
    public ApiResponse<Long> save(NewsCommentDTO newsCommentDTO) {

        Long id = newsCommentService.save(newsCommentDTO);

        return new ApiResponse<>(id);
    }

    @GetMapping("/client/news/comment/comment-reply-list-inner")
    public String replyList(@RequestParam Long commentId, Model model) {

        ApiResponse<?> response = newsCommentService.replyList(commentId);
        model.addAttribute("commentReplyList", response.getData());

        return "/client/news/fragments/comment-reply-list-inner";
    }

    @PostMapping("/client/news/comment/delete")
    @ResponseBody
    public ApiResponse<Boolean> deleteComment(NewsCommentDTO newsCommentDTO) {
        ApiResponse<Boolean> response = newsCommentService.delete(newsCommentDTO);
        return response;
    }

}
