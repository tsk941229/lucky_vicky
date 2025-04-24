package com.luckyvicky.web.client.news.dto;

import com.luckyvicky.web.client.news.entity.News;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {

    private Long id;
    private Long parentId;
    private String category;
    private String title;
    private String content;
    private String nickname;
    private String password;
    private int hits;
    private int likes;

    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private MultipartFile newsFile;



}
