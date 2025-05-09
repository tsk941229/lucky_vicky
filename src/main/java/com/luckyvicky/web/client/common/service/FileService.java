package com.luckyvicky.web.client.common.service;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.web.client.common.dto.FileDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;

@Service
public class FileService {

    public ResponseEntity<Resource> download(String fullPath) {

        try {
            File file = new File(fullPath);

            if(!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource fileResource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                    .body(fileResource);

        } catch (Exception e) {
            throw new RuntimeException("파일 다운로드 실패 :: FileService.download() ", e);
        }

    }

}
