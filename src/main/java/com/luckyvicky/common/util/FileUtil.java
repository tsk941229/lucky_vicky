package com.luckyvicky.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FileUtil {


    public Map<String, String> upload(MultipartFile file) {

        try {

            // 파일 초기화
            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
            long size = file.getSize();

            String prefix = "D:";

            // 경로 ex) /luckyvicky/upload/(newsFile)/(파일.확장자)
            String savePath = prefix + File.separator + "luckyvicky" + File.separator + "upload" + File.separator + file.getName() + File.separator;
            String saveName = UUID.randomUUID().toString();

            String fullPath = savePath + saveName + "." + extension;

            // 저장 할 경로에 디렉토리가 없을 때
            File uploadDirectory = new File(savePath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            // 파일 저장
            file.transferTo(new File(fullPath));

            // fileInfoMap 반환
            Map<String, String> fileInfoMap = new HashMap<String, String>();

            fileInfoMap.put("originalName", originalName);
            fileInfoMap.put("extension", extension);
            fileInfoMap.put("size", String.valueOf(size));
            fileInfoMap.put("savePath", savePath);
            fileInfoMap.put("saveName", saveName);
            fileInfoMap.put("fullPath", fullPath);

            return fileInfoMap;

        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패 :: FileUtil.upload()", e);
        }

    }


}
