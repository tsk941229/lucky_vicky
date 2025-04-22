package com.luckyvicky.web.client.news.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NewsDTO {

    private Long parentId;
    private String category;
    private String title;
    private String content;
    private String nickname;
    private String password;
    private int hits;
    private int likes;
    private MultipartFile newsFile;

}
