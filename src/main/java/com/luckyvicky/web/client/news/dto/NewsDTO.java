package com.luckyvicky.web.client.news.dto;

import com.luckyvicky.web.client.common.dto.FileDTO;
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
    private int depth;
    private Long groupId;
    private int orderNo;

    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    // 받을 때
    private MultipartFile newsFile;
    // 쓸 때
    private FileDTO newsFileDTO;

    private Long newsCommentCount;
    private List<NewsCommentDTO> newsCommentDTOList;


    // 목록 조회용 생성자 (parent, 파일, 댓글목록 빠짐 [수정될 수 있음])
    public NewsDTO(Long id, NewsCategoryEnum category, String title, String content, String nickname, String password, int hits, int likes, int depth, Long groupId, int orderNo, LocalDateTime createDt, LocalDateTime updateDt, Long newsCommentCount) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.hits = hits;
        this.likes = likes;
        this.depth = depth;
        this.groupId = groupId;
        this.orderNo = orderNo;
        this.createDt = createDt;
        this.updateDt = updateDt;
        this.newsCommentCount = newsCommentCount;
    }
}
