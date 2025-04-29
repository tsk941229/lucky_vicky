package com.luckyvicky.web.client.news.service;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.common.util.EncodeUtil;
import com.luckyvicky.web.client.news.dto.NewsCommentDTO;
import com.luckyvicky.web.client.news.entity.News;
import com.luckyvicky.web.client.news.entity.NewsComment;
import com.luckyvicky.web.client.news.repository.NewsCommentRepository;
import com.luckyvicky.web.client.news.repository.NewsRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.luckyvicky.web.client.news.entity.QNewsComment.newsComment;

@Service
@RequiredArgsConstructor
public class NewsCommentService {

    private final NewsRepository newsRepository;
    private final NewsCommentRepository newsCommentRepository;
    private final EncodeUtil encodeUtil;
    private final JPAQueryFactory jpaQueryFactory;

    // 댓글 저장
    @Transactional
    public Long save(NewsCommentDTO newsCommentDTO) {

        try {

            NewsComment parent = null;

            if(newsCommentDTO.getParentId() != null) {
                parent = newsCommentRepository.findById(newsCommentDTO.getParentId()).orElseThrow();
            }

            News news = newsRepository.findById(newsCommentDTO.getNewsId()).orElseThrow();

            NewsComment newsComment = NewsComment.builder()
                    .news(news)
                    .parent(parent)
                    .content(newsCommentDTO.getContent())
                    .nickname(newsCommentDTO.getNickname())
                    .password(encodeUtil.encode(newsCommentDTO.getPassword()))
                    .build();

            newsCommentRepository.save(newsComment);

            return newsComment.getId();

        } catch (Exception e) {
            throw new RuntimeException("News Comment 등록 실패 :: NewsCommentService.saveComment()", e);
        }

    }

    // 대댓글 조회
    @Transactional
    public ApiResponse<List<NewsCommentDTO>> replyList(Long commentId) {

        try {

            List<NewsCommentDTO> replyList = jpaQueryFactory
                    .select(Projections.constructor(NewsCommentDTO.class,
                            newsComment.id,
                            newsComment.nickname,
                            newsComment.password,
                            newsComment.content,
                            newsComment.createDt,
                            newsComment.updateDt
                    ))
                    .from(newsComment)
                    .where(newsComment.parent.id.eq(commentId))
                    .fetch();

            return new ApiResponse<>(replyList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // 댓글 삭제
    @Transactional
    public ApiResponse<Boolean> delete(NewsCommentDTO newsCommentDTO) {

        try {

            NewsComment newsComment = newsCommentRepository.findById(newsCommentDTO.getId()).orElseThrow();

            boolean isMatched = encodeUtil.matches(newsCommentDTO.getPassword(), newsComment.getPassword());

            if(isMatched) {
                newsCommentRepository.delete(newsComment);
            }

            return new ApiResponse<>(isMatched);

        } catch (Exception e) {
            throw new RuntimeException("News Comment 삭제 실패 :: NewsCommentService.delete()", e);
        }

    }

}
