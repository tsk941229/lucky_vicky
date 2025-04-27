package com.luckyvicky.web.client.common.controller;

import com.luckyvicky.web.client.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/client/file/download")
    @ResponseBody
    public ResponseEntity<Resource> download(@RequestParam String fullPath) {
        return fileService.download(fullPath);
    }

}
