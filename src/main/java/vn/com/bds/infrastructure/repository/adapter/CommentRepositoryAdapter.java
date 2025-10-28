package vn.com.bds.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.bds.domain.model.Comment;
import vn.com.bds.domain.repository.CommentRepository;
import vn.com.bds.infrastructure.repository.datajpa.CommentSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.CommentEntity;
import vn.com.bds.infrastructure.repository.mapper.CommentMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryAdapter implements CommentRepository {

    private final CommentSpringDataRepository springDataRepository;

    @Override
    public Comment save(Comment comment) {
        CommentEntity entity = CommentMapper.toEntity(comment);
        // Ensure relationships are correctly set before saving if mapper doesn't handle everything
        CommentEntity savedEntity = springDataRepository.save(entity);
        return CommentMapper.toDomain(savedEntity);
    }

    @Override
    public List<Comment> findByPostIdAndParentIsNull(UUID postId) {
        // Use the query method with eager fetching for user
        return springDataRepository.findByPostIdAndParentIsNullOrderByCreatedAtDesc(postId)
                .stream()
                .map(CommentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Comment> findById(UUID id) {
        return springDataRepository.findById(id).map(CommentMapper::toDomain);
    }

    @Override
    public void delete(Comment comment) {
        // Convert model to entity or just use ID if sufficient
        springDataRepository.deleteById(comment.getId());
    }
}