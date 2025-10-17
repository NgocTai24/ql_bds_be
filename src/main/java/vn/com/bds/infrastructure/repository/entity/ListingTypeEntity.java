package vn.com.bds.infrastructure.repository.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "listing_types")
public class ListingTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    // Một ListingType (Bán) có nhiều PropertyType (Nhà, Đất)
    @OneToMany(mappedBy = "listingType")
    private List<PropertyTypeEntity> propertyTypes;
}