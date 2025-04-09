package com.luckyvicky.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BoardController {

    @GetMapping("/board")
    @ResponseBody
    public ResponseEntity<Object> getBoard() {
        return ResponseEntity.ok("Board");
    }

}
