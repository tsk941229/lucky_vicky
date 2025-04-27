package com.luckyvicky.web.client.common.controller;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.client.common.dto.FileDTO;
import com.luckyvicky.web.client.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/client/file/download")
    @ResponseBody
    public ResponseEntity<Resource> download(FileDTO fileDTO) {
        return fileService.download(fileDTO);
    }

}
