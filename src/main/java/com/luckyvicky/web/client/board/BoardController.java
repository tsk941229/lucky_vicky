package com.luckyvicky.web.client.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping("/client/board/list")
    public String boardList() {
        return "/client/board/list";
    }

}
