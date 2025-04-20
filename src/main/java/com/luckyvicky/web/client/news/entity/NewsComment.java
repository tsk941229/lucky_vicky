package com.luckyvicky.web.client.news.entity;

import com.luckyvicky.web.client.common.entity.BaseEntity;
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
public class NewsComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    private NewsComment parent;

    @OneToMany(mappedBy = "parent")
    private List<NewsComment> replyList = new ArrayList<NewsComment>();

    private String content;

    private String nickname;

    private String password;

}
