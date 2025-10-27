package vn.com.bds.infrastructure.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.com.bds.infrastructure.repository.datajpa.ListingTypeSpringDataRepository;
import vn.com.bds.infrastructure.repository.datajpa.PropertyTypeSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.ListingTypeEntity;
import vn.com.bds.infrastructure.repository.entity.PropertyTypeEntity;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3) // Run after ListingTypeSeeder
public class PropertyTypeSeeder implements CommandLineRunner {

    private final PropertyTypeSpringDataRepository propertyTypeRepository;
    private final ListingTypeSpringDataRepository listingTypeRepository; // Need this to link

    @Override
    public void run(String... args) throws Exception {
        if (propertyTypeRepository.count() == 0) {
            log.info("Seeding PropertyTypes...");

            // Get the parent ListingType entities using the IDs from ListingTypeSeeder
            Optional<ListingTypeEntity> banOpt = listingTypeRepository.findById(ListingTypeSeeder.BAN_ID);
            Optional<ListingTypeEntity> choThueOpt = listingTypeRepository.findById(ListingTypeSeeder.CHO_THUE_ID);

            if (banOpt.isEmpty() || choThueOpt.isEmpty()) {
                log.error("Cannot seed PropertyTypes: Parent ListingTypes not found. Ensure ListingTypeSeeder ran correctly.");
                return;
            }
            ListingTypeEntity ban = banOpt.get();
            ListingTypeEntity choThue = choThueOpt.get();

            // Create Property Types linked to parents
            PropertyTypeEntity chungCuMini = new PropertyTypeEntity();
            chungCuMini.setName("Chung cư mini");
            chungCuMini.setListingType(ban); // Link to "Bán"

            PropertyTypeEntity chungCu = new PropertyTypeEntity();
            chungCu.setName("Chung Cư");
            chungCu.setListingType(choThue); // Link to "Cho Thuê" (as per your example, adjust if needed)

            PropertyTypeEntity bietThu = new PropertyTypeEntity();
            bietThu.setName("Biệt thự");
            bietThu.setListingType(choThue); // Link to "Cho Thuê" (as per your example, adjust if needed)

            PropertyTypeEntity datNen = new PropertyTypeEntity();
            datNen.setName("Đất nền");
            datNen.setListingType(ban); // Link to "Bán"

            propertyTypeRepository.saveAll(List.of(chungCuMini, chungCu, bietThu, datNen));
            log.info("PropertyTypes seeded.");

        } else {
            log.info("PropertyTypes already exist, skipping seeding.");
        }
    }
}