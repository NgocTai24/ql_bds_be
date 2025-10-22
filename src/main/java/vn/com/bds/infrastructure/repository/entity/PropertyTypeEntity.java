package vn.com.bds.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID; // <-- Import

@Data
@Entity
@Table(name = "property_types")
public class PropertyTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // <-- Generate UUID
    private UUID id; // <-- Change to UUID

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_type_id", nullable = false)
    private ListingTypeEntity listingType;
}