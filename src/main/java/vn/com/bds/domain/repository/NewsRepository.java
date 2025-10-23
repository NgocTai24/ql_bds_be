package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.News;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewsRepository {
    News save(News news);

    Optional<News> findById(UUID id);

    Optional<News> findBySlug(String slug);

    List<News> findAll();

    void deleteById(UUID id);
}