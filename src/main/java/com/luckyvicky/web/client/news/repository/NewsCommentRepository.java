package com.luckyvicky.web.client.news.repository;

import com.luckyvicky.web.client.news.entity.NewsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCommentRepository extends JpaRepository<NewsComment, Long> {
}
