package com.luckyvicky.web.client.news.repository;

import com.luckyvicky.web.client.news.entity.NewsFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFileRepository extends JpaRepository<NewsFile, Long> {
}
