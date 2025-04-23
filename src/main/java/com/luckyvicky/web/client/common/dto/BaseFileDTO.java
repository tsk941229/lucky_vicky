package com.luckyvicky.web.client.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class BaseFileDTO {

    private String originalName;
    private String saveName;
    private String savePath;
    private Long size;
    private String extension;

}
