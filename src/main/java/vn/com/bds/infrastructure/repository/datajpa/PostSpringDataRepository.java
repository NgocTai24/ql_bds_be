package vn.com.bds.infrastructure.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.PostEntity;

import java.util.List;
import java.util.UUID; // <-- Import

// Sửa kiểu ID thành UUID
public interface PostSpringDataRepository extends JpaRepository<PostEntity, UUID> {
    // --- ADD THIS METHOD ---
    /**
     * Finds all PostEntity objects, eagerly fetching the 'images' collection.
     * @EntityGraph tells JPA to load the specified attribute path ("images").
     * attributePaths: defines the relationship(s) to fetch eagerly.
     * type: FETCH means load it in the same query (JOIN FETCH).
     */
    @Override // We can override findAll if we always want images
    @EntityGraph(attributePaths = {"images", "user", "listingType", "propertyType"}, type = EntityGraph.EntityGraphType.FETCH)
    List<PostEntity> findAll();
}