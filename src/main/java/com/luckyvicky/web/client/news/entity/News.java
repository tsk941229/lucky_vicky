package com.luckyvicky.web.client.news.entity;


import com.luckyvicky.web.client.common.dto.FileDTO;
import com.luckyvicky.web.client.common.entity.BaseEntity;
import com.luckyvicky.web.client.news.dto.NewsCommentDTO;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.enums.NewsCategoryEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 셀프 참조
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id") // 조인컬럼 명시 안하면 <필드명>_id로 들어감 즉, 생략가능!
    private News parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<News> replyList = new ArrayList<News>();

    @Enumerated(EnumType.STRING)
    private NewsCategoryEnum category;

    /* 오라클은 CLOB으로 매핑된다는데 CLOB은 쓸 때 컨버터 만들어줘야할듯?
       지금은 포스트그레라 text로 매핑되니 상관없음 */
    @Lob
    private String content;

    private String title;
    private String nickname;
    private String password;
    private int hits;
    private int likes;
    private int depth;
    private Long groupId;
    private int orderNo;


    // 파일DTO, 댓글DTO List랑 같이
    public static NewsDTO toDTO(News news, FileDTO newsFileDTO, List<NewsCommentDTO> newsCommentDTOList) {
        return NewsDTO.builder()
                .id(news.getId())
                .parentId(news.getParent() != null ? news.getParent().getId() : null)
                .category(news.getCategory())
                .title(news.getTitle())
                .content(news.getContent())
                .nickname(news.getNickname())
                .password(news.getPassword())
                .hits(news.getHits())
                .likes(news.getLikes())
                .depth(news.getDepth())
                .createDt(news.getCreateDt())
                .updateDt(news.getUpdateDt())
                .newsFileDTO(newsFileDTO)
                .newsCommentDTOList(newsCommentDTOList)
                .build();
    }

    public void increaseHits() {
        this.hits++;
    }

}
