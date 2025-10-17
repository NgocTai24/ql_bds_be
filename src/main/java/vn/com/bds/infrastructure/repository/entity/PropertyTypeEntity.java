package vn.com.bds.infrastructure.repository.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "property_types")
public class PropertyTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    // Một PropertyType (Nhà) thuộc về một ListingType (Bán)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_type_id", nullable = false)
    private ListingTypeEntity listingType;
}