package vn.com.bds.infrastructure.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.CommentEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentSpringDataRepository extends JpaRepository<CommentEntity, UUID> {

    // SỬA DÒNG NÀY: Thêm "post" vào attributePaths
    @EntityGraph(attributePaths = {"user", "post"}, type = EntityGraph.EntityGraphType.FETCH)
    List<CommentEntity> findByPostIdAndParentIsNullOrderByCreatedAtDesc(UUID postId);

    // THÊM PHƯƠNG THỨC NÀY ĐỂ LẤY REPLIES (tải luôn user)
    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.FETCH)
    List<CommentEntity> findByParentIdOrderByCreatedAtAsc(UUID parentId);

    // findById đã có @EntityGraph("user", "post") từ trước (để kiểm tra parent)
    @Override
    @EntityGraph(attributePaths = {"user", "post"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<CommentEntity> findById(UUID id);
}