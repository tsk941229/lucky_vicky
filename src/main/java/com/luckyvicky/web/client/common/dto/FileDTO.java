package com.luckyvicky.web.client.common.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class FileDTO {

    private Long id;

    private String originalName;
    private String extension;
    private Long size;
    private String savePath;
    private String saveName;
    // 받을 때
    private LocalDateTime createDt;
    private LocalDateTime updateDt;


    public FileDTO(Long id, String originalName, String extension, Long size, String savePath, String saveName, LocalDateTime createDt, LocalDateTime updateDt) {
        this.id = id;
        this.originalName = originalName;
        this.extension = extension;
        this.size = size;
        this.savePath = savePath;
        this.saveName = saveName;
        this.createDt = createDt;
        this.updateDt = updateDt;
    }
}
