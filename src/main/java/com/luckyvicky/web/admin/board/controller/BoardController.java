package com.luckyvicky.web.admin.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class BoardController {

    @GetMapping("/admin/board")
    @ResponseBody
    public ResponseEntity<Object> getBoard() {
        return ResponseEntity.ok("Admin Board");
    }

}
