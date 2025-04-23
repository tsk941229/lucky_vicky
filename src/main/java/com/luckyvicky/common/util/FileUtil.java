package com.luckyvicky.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class FileUtil {


    public void upload(MultipartFile file) {

        // 파일 초기화
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        long size = file.getSize();
        String savePath = File.separator + "luckyvicky" + File.separator + "upload" + File.separator + file.getName() + File.separator;
        String saveName = UUID.randomUUID().toString();

        System.out.println("originalName = " + originalName);
        System.out.println("extension = " + extension);
        System.out.println("saveName = " + saveName);
        System.out.println("savePath = " + savePath);
        System.out.println("size = " + size);


    }


}
