package com.luckyvicky.web.client.news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewsController {

    @GetMapping("/client/news/list")
    public String list() {
        return "/client/news/list";
    }

    @GetMapping("/client/news/form")
    public String form() {
        return "/client/news/form";
    }

}
