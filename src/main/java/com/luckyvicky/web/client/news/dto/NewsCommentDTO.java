package com.luckyvicky.web.client.news.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsCommentDTO {

    private Long id;
    private Long parentId;
    private Long newsId;
    private String nickname;
    private String password;
    private String content;

    private LocalDateTime createDt;
    private LocalDateTime updateDt;

}
