package vn.com.bds.infrastructure.repository.adapter;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.bds.domain.model.Post;
import vn.com.bds.domain.repository.PostRepository;
import vn.com.bds.infrastructure.repository.datajpa.PostSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.PostEntity;
import vn.com.bds.infrastructure.repository.mapper.PostMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {

    private final PostSpringDataRepository springDataRepository;

    @Override
    public Post save(Post post) {
        PostEntity entity = PostMapper.toEntity(post);
        PostEntity savedEntity = springDataRepository.save(entity);
        return PostMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Post> findById(UUID id) { // <-- Sửa thành UUID
        return springDataRepository.findById(id).map(PostMapper::toDomain);
    }

    @Override
    public List<Post> findAll() {
        // This now calls the @EntityGraph version from PostSpringDataRepository
        // and PostMapper.toDomain will handle the loaded images.
        return springDataRepository.findAll().stream()
                .map(PostMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) { // <-- Sửa thành UUID
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return springDataRepository.existsById(id);
    }
}