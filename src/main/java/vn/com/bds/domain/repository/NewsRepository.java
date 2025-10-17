package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.News;

import java.util.List;
import java.util.Optional;

public interface NewsRepository {
    News save(News news);

    Optional<News> findById(Long id);

    Optional<News> findBySlug(String slug);

    List<News> findAll();

    void deleteById(Long id);
}