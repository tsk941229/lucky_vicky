package com.luckyvicky.web.client.news.dto;

import com.luckyvicky.web.client.news.entity.News;
import com.luckyvicky.web.client.news.enums.NewsCategoryEnum;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {

    private Long id;
    private Long parentId;
    private NewsCategoryEnum category;
    private String title;
    private String content;
    private String nickname;
    private String password;
    private int hits;
    private int likes;

    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private MultipartFile newsFile;

    private Long newsCommentCount;
    private List<NewsCommentDTO> newsCommentDTOList;


    // 목록 조회용 생성자 (parent, 파일, 댓글목록 빠짐 [수정될 수 있음])
    public NewsDTO(Long id, NewsCategoryEnum category, String title, String content, String nickname, String password, int hits, int likes, LocalDateTime createDt, LocalDateTime updateDt, Long newsCommentCount) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.hits = hits;
        this.likes = likes;
        this.createDt = createDt;
        this.updateDt = updateDt;
        this.newsCommentCount = newsCommentCount;
    }
}
