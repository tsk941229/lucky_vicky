package com.luckyvicky.web.client.news.service;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.common.util.*;
import com.luckyvicky.common.vo.PageVO;
import com.luckyvicky.web.client.common.dto.FileDTO;
import com.luckyvicky.web.client.news.dto.NewsCommentDTO;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.dto.NewsSearchDTO;
import com.luckyvicky.web.client.news.entity.*;
import com.luckyvicky.web.client.news.enums.NewsCategoryEnum;
import com.luckyvicky.web.client.news.repository.NewsCommentRepository;
import com.luckyvicky.web.client.news.repository.NewsFileRepository;
import com.luckyvicky.web.client.news.repository.NewsRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.luckyvicky.web.client.news.entity.QNews.news;
import static com.luckyvicky.web.client.news.entity.QNewsComment.newsComment;
import static com.luckyvicky.web.client.news.entity.QNewsFile.newsFile;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsFileRepository newsFileRepository;
    private final NewsCommentRepository newsCommentRepository;

    private final JPAQueryFactory jpaQueryFactory;

    private final EncodeUtil encodeUtil;
    private final FileUtil fileUtil;
    private final PageUtil pageUtil;
    private final CookieUtil cookieUtil;

    @Transactional
    public Long save(NewsDTO newsDTO) {

        try {

            News parent = null;
            Long groupId = null;
            int orderNo = 0;

            // 답글일 때 (부모 있음)
            if(newsDTO.getParentId() != null){
                parent = newsRepository.findById(newsDTO.getParentId()).orElseThrow();
                groupId = parent.getGroupId();
                orderNo = parent.getOrderNo() + 1;

                // orderNo 밀어내기
                jpaQueryFactory
                        .update(news)
                        .set(news.orderNo, news.orderNo.add(1))
                        .where(news.groupId.eq(groupId)               // groupId == news.groupId
                                .and(news.orderNo.goe(orderNo))       // orderNo >= news.orderNo
                                .and(news.depth.ne(0)))          // depth != 0
                        .execute();
            }

            // News 등록
            News news = News.builder()
                    .parent(parent)
                    .category(newsDTO.getCategory())
                    .title(newsDTO.getTitle())
                    .content(newsDTO.getContent())
                    .nickname(newsDTO.getNickname())
                    .password(encodeUtil.encode(newsDTO.getPassword()))
                    .depth(newsDTO.getDepth())
                    .groupId(groupId)
                    .orderNo(orderNo)
                    .build();

            newsRepository.save(news);

            // 최상위 게시글은 save직후 groupId를 id로 넣어줌
            if(news.getGroupId() == null){
                jpaQueryFactory
                        .update(QNews.news)
                        .set(QNews.news.groupId, news.getId())
                        .where(QNews.news.id.eq(news.getId()))
                        .execute();
            }


            // NewsFile 등록
            if(newsDTO.getNewsFile() != null && !newsDTO.getNewsFile().isEmpty()) {

                MultipartFile file = newsDTO.getNewsFile();

                // 업로드
                FileDTO fileDTO = fileUtil.upload(file);

                NewsFile newsFile = NewsFile.builder()
                        .news(news)
                        .originalName(fileDTO.getOriginalName())
                        .extension(fileDTO.getExtension())
                        .saveName(fileDTO.getSaveName())
                        .savePath(fileDTO.getSavePath())
                        .size(fileDTO.getSize())
                        .build();

                newsFileRepository.save(newsFile);

            }

            return news.getId();

        } catch (Exception e) {
            throw new RuntimeException("News 등록 실패 :: NewsService.save()", e);
        }

    }


    @Transactional
    public ApiResponse<List<NewsDTO>> findNewsPage(NewsSearchDTO newsSearchDTO) {

        try {

            //! TODO (삭제 데이터 고려)
            Long totalCount = jpaQueryFactory
                    .select(news.count())
                    .from(news)
                    .where(titleOrContentContains(newsSearchDTO.getKeyword()))
                    .fetchOne();

            // 페이징 정보
            PageVO pageVO = pageUtil.getPageVO(newsSearchDTO, totalCount);

            // 생성자 순서 맞춰야 함 TODO: 답글 할 때 셀프참조 구현해야함
            List<NewsDTO> newsDTOList = jpaQueryFactory
                    .select(Projections.constructor(NewsDTO.class,
                            news.id,
                            news.category,
                            news.title,
                            news.content,
                            news.nickname,
                            news.password,
                            news.hits,
                            news.likes,
                            news.depth,
                            news.groupId,
                            news.orderNo,
                            news.createDt,
                            news.updateDt,
                            newsComment.count()
                    ))
                    .from(news)
                    .leftJoin(newsComment)
                    .on(news.id.eq(newsComment.news.id)).fetchJoin()
                    .where(newsComment.parent.isNull()
                            .and(titleOrContentContains(newsSearchDTO.getKeyword())))
                    .groupBy(news.id, news.parent.id)
                    .orderBy(news.groupId.desc(), news.orderNo.asc(), news.createDt.desc())
                    .limit(pageVO.getLimit())
                    .offset(pageVO.getStartIndex())
                    .fetch();


            HashMap<String, Object> metaData = new HashMap<>();
            metaData.put("pageVO", pageVO);

            return new ApiResponse(newsDTOList, metaData);

        } catch (Exception e) {
            throw new RuntimeException("News Page 조회 실패 :: NewsService.findNewsPage()", e);
        }

    }

    // news title, content like %% (keyword가 null일 때 null 반환 -> where절 무시)
    private BooleanExpression titleOrContentContains(String keyword) {
        if(!StringUtils.hasText(keyword)) return null;

        return QNews.news.title.containsIgnoreCase(keyword).or(QNews.news.content.containsIgnoreCase(keyword));
    }


    /**
     * 뉴스, 뉴스댓글, 뉴스 파일 조회 (NewsDTO)
     */
    @Transactional
    public ApiResponse<NewsDTO> findNewsForDetail(long id, HttpServletResponse response, HttpServletRequest request) {

        try {
            News news = newsRepository.findById(id).orElseThrow();

            // 조회수 추가
            increaseHits(news, response, request);

            // 파일 조회
            FileDTO newsFileDTO = jpaQueryFactory
                    .select(Projections.constructor(FileDTO.class,
                            newsFile.id,
                            newsFile.originalName,
                            newsFile.extension,
                            newsFile.size,
                            newsFile.savePath,
                            newsFile.saveName,
                            newsFile.createDt,
                            newsFile.updateDt
                    ))
                    .from(newsFile)
                    .where(newsFile.news.id.eq(id))
                    .fetchOne();

            // 대댓글 count 서브쿼리 하기 위함
            QNewsComment comment = new QNewsComment("comment");
            QNewsComment commentReply = new QNewsComment("commentReply");

            // 댓글 가져오기
            List<NewsCommentDTO> newsCommentDTOList = jpaQueryFactory
                    .select(Projections.constructor(NewsCommentDTO.class,
                            comment.id,
                            comment.nickname,
                            comment.password,
                            comment.content,
                            comment.createDt,
                            comment.updateDt,
                            JPAExpressions
                                    .select(commentReply.count())
                                    .from(commentReply)
                                    .where(commentReply.parent.id.eq(comment.id))
                    ))
                    .from(comment)
                    .where(comment.news.id.eq(news.getId())
                            .and(comment.parent.isNull()))
                    .orderBy(comment.id.desc())
                    .fetch();

            // 파일, 댓글 목록이랑 같이
            NewsDTO newsDTO = News.toDTO(news, newsFileDTO, newsCommentDTOList);

            return new ApiResponse<>(newsDTO);

        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("News 조회 실패 :: NewsService.findNews()", e);
        }

    }

    /**
     * 좋아요 여부 체크
     */
    public boolean checkLikes(long id, HttpServletRequest request) {

        // cookie name : likedNewsIdList

        List<String> likedIdList = cookieUtil.getCookieValueList(request, "likedNewsIdList");

        if(likedIdList.isEmpty()) {
            return false;
        }

        boolean result = likedIdList.stream().anyMatch(likedId -> likedId.equals(String.valueOf(id)));

        return result;

    }

    /**
     * 좋아요 적용
     */
    @Transactional
    public void toggleLikes(long id, boolean isUp, HttpServletRequest request, HttpServletResponse response) {

        try {

            News news = newsRepository.findById(id).orElseThrow();

            List<String> likedIdList = cookieUtil.getCookieValueList(request, "likedNewsIdList");

            if(isUp) {
                // 좋아요 -> 좋아요+1, 쿠키에 id 등록
                news.increaseLikes();
                cookieUtil.addCookie(response, "likedNewsIdList", List.of(String.valueOf(id)));
            }

            if(!isUp) {
                // 좋아요 취소 -> 좋아요-1, 쿠키에서 id 제거
                news.decreaseLikes();
            }

        } catch (Exception e) {
            throw new RuntimeException("News 좋아요 적용 실패 :: NewsService.toggleLikes()", e);
        }

    }

    /**
     * 조회수 추가 TODO: 쿠키이름 상수로
     */
    private void increaseHits(News news, HttpServletResponse response, HttpServletRequest request) {

        List<String> viewedIdList = cookieUtil.getCookieValueList(request, "viewedNewsIdList");

        // 쿠키값 없을 때
        if(CollectionUtils.isEmpty(viewedIdList)) {
            news.increaseHits();
            cookieUtil.addCookie(response, "viewedNewsIdList", List.of(String.valueOf(news.getId())));
            return;
        }

        boolean isViewed = false;
        isViewed = viewedIdList.contains(String.valueOf(news.getId()));

        if(!isViewed){
            news.increaseHits();
            viewedIdList.add(String.valueOf(news.getId()));
            cookieUtil.addCookie(response, "viewedNewsIdList", viewedIdList);
        }

    }

    @Transactional
    public ApiResponse<NewsDTO> findNewsForUpdate(long id) {

        try {
            News news = newsRepository.findById(id).orElseThrow();

            // 파일 조회
            FileDTO newsFileDTO = jpaQueryFactory
                    .select(Projections.constructor(FileDTO.class,
                            newsFile.id,
                            newsFile.originalName,
                            newsFile.extension,
                            newsFile.size,
                            newsFile.savePath,
                            newsFile.saveName,
                            newsFile.createDt,
                            newsFile.updateDt
                    ))
                    .from(newsFile)
                    .where(newsFile.news.id.eq(id))
                    .fetchOne();

            // 대댓글 count 서브쿼리 하기 위함
            QNewsComment comment = new QNewsComment("comment");
            QNewsComment commentReply = new QNewsComment("commentReply");

            // 댓글 가져오기
            List<NewsCommentDTO> newsCommentDTOList = jpaQueryFactory
                    .select(Projections.constructor(NewsCommentDTO.class,
                            comment.id,
                            comment.nickname,
                            comment.password,
                            comment.content,
                            comment.createDt,
                            comment.updateDt,
                            JPAExpressions
                                    .select(commentReply.count())
                                    .from(commentReply)
                                    .where(commentReply.parent.id.eq(comment.id))
                    ))
                    .from(comment)
                    .where(comment.news.id.eq(news.getId())
                            .and(comment.parent.isNull()))
                    .orderBy(comment.id.desc())
                    .fetch();

            // 파일, 댓글 목록이랑 같이
            NewsDTO newsDTO = News.toDTO(news, newsFileDTO, newsCommentDTOList);

            return new ApiResponse<>(newsDTO);

        } catch(Exception e) {
            throw new RuntimeException("News 조회 실패 :: NewsService.findNews()", e);
        }

    }


    @Transactional
    public ApiResponse<Boolean> delete(NewsDTO newsDTO) {

        try {

            News news = newsRepository.findById(newsDTO.getId()).orElseThrow();

            boolean isMatched = encodeUtil.matches(newsDTO.getPassword(), news.getPassword());

            if(isMatched) {

                // TODO: 삭제할 때 연관되어 있는 모든 데이터 삭제할지, 삭제컬럼 만들어서 삭제여부 정하고, 삭제된 게시글 표시 할지

                // 댓글 모두 지우기 (임시)
                List<Long> newsCommentIdList = jpaQueryFactory
                        .select(newsComment.id)
                        .from(newsComment)
                        .where(newsComment.news.id.eq(news.getId()))
                        .fetch();

                // 답글 모두 지우기 (임시)
                List<Long> newsIdList = jpaQueryFactory
                        .select(QNews.news.id)
                        .from(QNews.news)
                        .where(QNews.news.parent.id.eq(news.getId()))
                        .fetch();

                newsCommentRepository.deleteAllById(newsCommentIdList);
                newsRepository.deleteAllById(newsIdList);
                newsRepository.delete(news);
            }

            return new ApiResponse<>(isMatched);

        } catch (Exception e) {
            throw new RuntimeException("News 삭제 실패 :: NewsService.delete()", e);
        }

    }

    @Transactional
    public ApiResponse<Boolean> matchPassword(NewsDTO newsDTO) {

        try{

            News news = newsRepository.findById(newsDTO.getId()).orElseThrow();
            boolean isMatched = encodeUtil.matches(newsDTO.getPassword(), news.getPassword());

            return new ApiResponse<>(isMatched);

        } catch (Exception e) {
            throw new RuntimeException("News 조회 실패 :: NewsService.matchPassword()", e);
        }
    }

    @Transactional
    public Long update(NewsDTO newsDTO) {

        try {

            // news 업데이트
            jpaQueryFactory
                    .update(news)
                    .set(news.category, newsDTO.getCategory())
                    .set(news.title, newsDTO.getTitle())
                    .set(news.nickname, newsDTO.getNickname())
                    .set(news.password, encodeUtil.encode(newsDTO.getPassword()))
                    .set(news.content, newsDTO.getContent())
                    .where(news.id.eq(newsDTO.getId()))
                    .execute();

            // 파일 업데이트
            if(newsDTO.getNewsFile() != null && !newsDTO.getNewsFile().isEmpty()) {

                MultipartFile file = newsDTO.getNewsFile();

                // 업로드
                FileDTO fileDTO = fileUtil.upload(file);

                // TODO: 업로드된 파일도 삭제할지 생각해보자
                // 파일 삭제 후 신규 저장
                jpaQueryFactory
                        .delete(newsFile)
                        .where(newsFile.news.id.eq(newsDTO.getId()))
                        .execute();

                News news = newsRepository.findById(newsDTO.getId()).orElseThrow();

                NewsFile newsFile = NewsFile.builder()
                        .news(news)
                        .originalName(fileDTO.getOriginalName())
                        .extension(fileDTO.getExtension())
                        .saveName(fileDTO.getSaveName())
                        .savePath(fileDTO.getSavePath())
                        .size(fileDTO.getSize())
                        .build();

                newsFileRepository.save(newsFile);
            }

            return newsDTO.getId();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("News 수정 실패 :: NewsService.update()", e);
        }

    }

}
