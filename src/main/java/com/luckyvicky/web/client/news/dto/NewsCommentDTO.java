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
    private Integer depth;

    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    // parent, news, depth 제외
    public NewsCommentDTO(Long id, String nickname, String password, String content, LocalDateTime createDt, LocalDateTime updateDt) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.content = content;
        this.createDt = createDt;
        this.updateDt = updateDt;
    }
}
