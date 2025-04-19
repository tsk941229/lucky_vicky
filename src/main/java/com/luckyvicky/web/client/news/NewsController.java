package com.luckyvicky.web.client.news;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewsController {

    @GetMapping("/client/news/list")
    public String newsList() {
        return "/client/news/list";
    }

}
