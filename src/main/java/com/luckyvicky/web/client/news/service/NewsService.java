package com.luckyvicky.web.client.news.service;

import com.luckyvicky.common.response.ApiResponse;
import com.luckyvicky.common.util.EncodeUtil;
import com.luckyvicky.common.util.FileUtil;
import com.luckyvicky.common.util.PageUtil;
import com.luckyvicky.common.vo.PageVO;
import com.luckyvicky.web.client.news.dto.NewsCommentDTO;
import com.luckyvicky.web.client.news.dto.NewsDTO;
import com.luckyvicky.web.client.news.dto.NewsSearchDTO;
import com.luckyvicky.web.client.news.entity.News;
import com.luckyvicky.web.client.news.entity.NewsComment;
import com.luckyvicky.web.client.news.entity.NewsFile;
import com.luckyvicky.web.client.news.entity.QNewsComment;
import com.luckyvicky.web.client.news.enums.NewsCategoryEnum;
import com.luckyvicky.web.client.news.repository.NewsCommentRepository;
import com.luckyvicky.web.client.news.repository.NewsFileRepository;
import com.luckyvicky.web.client.news.repository.NewsRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.luckyvicky.web.client.news.entity.QNews.news;
import static com.luckyvicky.web.client.news.entity.QNewsComment.newsComment;

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

    @Transactional
    public Long save(NewsDTO newsDTO) {

        try {

            // News 등록
            News news = News.builder()
                    .category(newsDTO.getCategory())
                    .title(newsDTO.getTitle())
                    .content(newsDTO.getContent())
                    .nickname(newsDTO.getNickname())
                    .password(encodeUtil.encode(newsDTO.getPassword()))
                    .build();

            newsRepository.save(news);

            // NewsFile 등록
            if(newsDTO.getNewsFile() != null && !newsDTO.getNewsFile().isEmpty()) {

                MultipartFile file = newsDTO.getNewsFile();

                // 업로드
                Map<String, String> fileInfoMap = fileUtil.upload(file);

                NewsFile newsFile = NewsFile.builder()
                        .news(news)
                        .originalName(fileInfoMap.get("originalName"))
                        .extension(fileInfoMap.get("extension"))
                        .saveName(fileInfoMap.get("saveName"))
                        .savePath(fileInfoMap.get("savePath"))
                        .size(Long.valueOf(fileInfoMap.get("size")))
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
                    .fetchOne();

            // 페이징 정보
            PageVO pageVO = pageUtil.getPageVO(newsSearchDTO, totalCount);

            // 뉴스 목록 조회
            List<News> newsList = jpaQueryFactory
                    .select(news)
                    .from(news)
                    .orderBy(news.id.desc())
                    .limit(pageVO.getLimit())
                    .offset(pageVO.getStartIndex())
                    .fetch();

            List<NewsDTO> newsDTOList = newsList.stream()
                    .map(news -> News.toDTO(news))
                    .collect(Collectors.toList());


            HashMap<String, Object> metaData = new HashMap<>();
            metaData.put("pageVO", pageVO);

            return new ApiResponse(newsDTOList, metaData);

        } catch (Exception e) {
            throw new RuntimeException("News Page 조회 실패 :: NewsService.findNewsPage()", e);
        }

    }


    @Transactional
    public ApiResponse<NewsDTO> findNews(long id) {

        try {
            News news = newsRepository.findById(id).orElseThrow();

            // 댓글 조회
            List<NewsComment> newsCommentList = jpaQueryFactory
                    .select(newsComment)
                    .from(newsComment)
                    .where(newsComment.news.id.eq(news.getId()))
                    .orderBy(newsComment.id.desc())
                    .fetch();

            // 프로젝션 쓰는걸로 수정해도 됨
            List<NewsCommentDTO> newsCommentDTOList = newsCommentList.stream()
                    .map(newsComment -> NewsComment.toDTO(newsComment))
                    .collect(Collectors.toList());

            // 댓글 목록이랑 같이
            NewsDTO newsDTO = News.toDTO(news, newsCommentDTOList);

            return new ApiResponse<>(newsDTO);

        } catch(Exception e) {
            throw new RuntimeException("News 조회 실패 :: NewsService.findNews()", e);
        }

    }


    // 댓글 저장
    @Transactional
    public Long saveComment(NewsCommentDTO newsCommentDTO) {

        try {

            News news = newsRepository.findById(newsCommentDTO.getNewsId()).orElseThrow();

            NewsComment newsComment = NewsComment.builder()
                    .news(news)
                    .content(newsCommentDTO.getContent())
                    .nickname(newsCommentDTO.getNickname())
                    .password(encodeUtil.encode(newsCommentDTO.getPassword()))
                    .build();

            newsCommentRepository.save(newsComment);

            return newsComment.getId();

        } catch (Exception e) {
            throw new RuntimeException("News Comment 등록 실패 :: NewsService.saveComment()", e);
        }

    }

}
