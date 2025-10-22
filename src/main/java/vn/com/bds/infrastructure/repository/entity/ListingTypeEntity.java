package vn.com.bds.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID; // <-- Import

@Data
@Entity
@Table(name = "listing_types")
public class ListingTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // <-- Bảo DB tự sinh UUID
    private UUID id; // <-- Sửa thành UUID

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "listingType")
    private List<PropertyTypeEntity> propertyTypes;
}