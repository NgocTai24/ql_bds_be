package vn.com.bds.infrastructure.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.com.bds.infrastructure.repository.datajpa.ListingTypeSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.ListingTypeEntity;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after UserSeeder
public class ListingTypeSeeder implements CommandLineRunner {

    private final ListingTypeSpringDataRepository listingTypeRepository;

    // Keep track of generated IDs for PropertyTypeSeeder
    public static UUID BAN_ID;
    public static UUID CHO_THUE_ID;

    @Override
    public void run(String... args) throws Exception {
        if (listingTypeRepository.count() == 0) {
            log.info("Seeding ListingTypes...");
            ListingTypeEntity ban = new ListingTypeEntity();
            ban.setName("Bán");
            ListingTypeEntity savedBan = listingTypeRepository.save(ban);
            BAN_ID = savedBan.getId(); // Store the ID

            ListingTypeEntity choThue = new ListingTypeEntity();
            choThue.setName("Cho Thuê");
            ListingTypeEntity savedChoThue = listingTypeRepository.save(choThue);
            CHO_THUE_ID = savedChoThue.getId(); // Store the ID

            log.info("ListingTypes seeded.");
        } else {
            log.info("ListingTypes already exist, skipping seeding.");
            // Optionally fetch existing IDs if needed
            listingTypeRepository.findAll().forEach(lt -> {
                if ("Bán".equalsIgnoreCase(lt.getName())) BAN_ID = lt.getId();
                if ("Cho Thuê".equalsIgnoreCase(lt.getName())) CHO_THUE_ID = lt.getId();
            });
        }
    }
}