package vn.com.bds.infrastructure.seeder;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.com.bds.domain.model.*; // Import domain models
// Import Infrastructure components
import vn.com.bds.infrastructure.repository.datajpa.PostSpringDataRepository;
import vn.com.bds.infrastructure.repository.datajpa.PropertyTypeSpringDataRepository; // Keep this
import vn.com.bds.infrastructure.repository.datajpa.UserSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.ImageEntity;
import vn.com.bds.infrastructure.repository.entity.ListingTypeEntity;
import vn.com.bds.infrastructure.repository.entity.PostEntity;
import vn.com.bds.infrastructure.repository.entity.PropertyTypeEntity; // Keep this
import vn.com.bds.infrastructure.repository.entity.UserEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID; // Keep UUID import

@Component
@RequiredArgsConstructor
@Slf4j
@Order(4) // Ensure this runs AFTER UserSeeder(1), ListingTypeSeeder(2), PropertyTypeSeeder(3)
public class PostSeeder implements CommandLineRunner {

    private final PostSpringDataRepository postSpringDataRepository;
    private final UserSpringDataRepository userSpringDataRepository;
    private final PropertyTypeSpringDataRepository propertyTypeSpringDataRepository; // Used to fetch types
    private final Faker faker = new Faker(new Locale("vi"));
    private final Random random = new Random();

    // Sample Real Estate Image URLs
    private static final List<String> SAMPLE_IMAGES = List.of(
            "https://noithattugia.com/wp-content/uploads/2024/08/mau-nha-dep-anh-dai-dien-2024-scaled.jpg",
            "https://nhadepshouse.com/picture/file/thiet-ke-biet-thu-hien-dai-anh-minh-vip1-vcn_(1).jpg",
            "https://vinavic.vn/images/projects/2023/12/26/resized/mau-nha-3-tang-dep-hien-dai-mai-bang-noi-bat-14-20m-bt2228-13_1703581264.jpg",
            "https://kisato.vn/wp-content/uploads/2022/05/4-3.jpg",
            "https://wedo.vn/wp-content/uploads/2019/01/nha-cap-4-dep-nhat-viet-nam-12.jpg",
            "https://angcovat.vn/imagesdata/BT5110521/1-mau-thiet-ke-nha-10x15m-3-phong-ngu.jpg",
            "https://bizweb.dktcdn.net/100/114/816/files/nha-cap-4-mai-thai-mau-do.jpg?v=1638776020519",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHzL-m1ZFH1lSGdWGHfgx1B_6rJx1ee7B5WFZ7K9Rcfl4NxLnmEG4qcGOagICZ6lD4APU&usqp=CAU",
            "https://luxviet.vn/wp-content/uploads/2021/09/Mau-nha-cap-4-dep-150m2-4-phong-ngu-luxviet-11038-01.jpg",
            "https://thietkethicongnhadep.net/wp-content/uploads/2019/07/mau-thiet-ke-biet-thu-vuon-1-tang-NDBT1T89-600x450.jpg",
            "https://nhadeptana.vn/wp-content/uploads/2021/11/mau-nha-vuon-cap-4-dep-tai-son-la-4.jpg",
            "https://kisato.vn/wp-content/uploads/2021/11/Mau-nha-cap-4-mai-Nhat-dep-xieu-long-gia-chu-tai-Thai-Binh-5.jpg",
            "https://levan.vn/wp-content/uploads/2023/09/mau-nha-vuon-dep-1-tang-11.jpg",
            "https://kientrucsviet.com/wp-content/uploads/2020/09/mau-nha-1-tang-dep.jpg",
            "https://cdn.hita.com.vn/storage/blog/meo-vat-gia-dinh/anh-ngoi-nha-16.jpg",
            "https://ichef.bbci.co.uk/ace/ws/640/cpsprodpb/16653/production/_105413719_b272002b-80f3-4667-a235-62627f519143.jpg.webp",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRC7HKdomKKUqENxv1dKMqMFU6nKRwdlSUywQpdTky6tYFjUO41FH_Ydiid07LiAZm-knw&usqp=CAU",
            "https://c.files.bbci.co.uk/0AAB/production/_105413720_b272002b-80f3-4667-a235-62627f519143.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIO9-FV0Z43AXtqsdfcMWQBMjKvWRIDcHmIBBZz_e-K_RJA9ucgbTZFMXQgNbLXHxzIHY&usqp=CAU",
            "https://akisa.vn/uploads/plugin/product_items/13551/mau-biet-thu-nha-dep-2-tang-hien-dai-bt21377-v2.jpg",
            "https://acd.vn/wp-content/uploads/2021/09/thiet-ke-nha-dep-vinh-phuc-10-1067x800.jpg",
            "https://vking.vn/wp-content/uploads/2023/07/design-nha-dep-3tang-8.jpg",
            "https://vking.vn/wp-content/uploads/2023/08/thiet-ke-mau-nha-2-tang-dep-o-nong-thon-2.jpg",
            "https://xaydungminhthinh.com/wp-content/uploads/2020/02/mau-nha-cap-4-dep-22-jpg.webp",
            "https://neohouse.vn/wp-content/uploads/2020/10/thiet-ke-nha-cap-4-dep-bia.jpg",
            "https://xaydungtlt.com/wp-content/uploads/2024/01/mau-nha-biet-thu-hinh6.jpg",
            "https://igcons.vn/wp-content/uploads/2024/09/nha-cap-4-mai-chu-a-1170x608.jpg",
            "https://tostemvietnam.com/wp-content/uploads/2023/06/nha-mai-thai-dep-7-1.png",
            "https://khonhamaudep.com/wp-content/uploads/2024/12/nha-cap-4-mai-chu-a-1.jpg",
            "https://vking.vn/wp-content/uploads/2023/07/design-nha-dep-3tang-1.jpg",
            "https://danviet.mediacdn.vn/296231569849192448/2023/1/3/nha-dep-o-binh-duong-33-1672785048203346048834.jpg",
            "https://sbshouse.vn/wp-content/uploads/2023/02/1-6.jpg"
    );

    // REMOVE the hardcoded UUID list
    // private static final List<UUID> PROPERTY_TYPE_IDS = List.of(...);


    @Override
    public void run(String... args) throws Exception {
        seedPosts();
    }

    private void seedPosts() {
        if (postSpringDataRepository.count() == 0) {
            log.info("Seeding Posts...");

            // --- MODIFIED SECTION ---
            // Fetch prerequisite entities directly from DB
            List<UserEntity> users = userSpringDataRepository.findAll();
            // Fetch ALL available PropertyType entities created by PropertyTypeSeeder
            List<PropertyTypeEntity> propertyTypes = propertyTypeSpringDataRepository.findAllWithListingType();
            // --- END MODIFIED SECTION ---

            if (users.isEmpty() || propertyTypes.isEmpty()) {
                log.warn("Cannot seed Posts: Users or PropertyTypes not found in database. Make sure UserSeeder and category seeders run first and created data.");
                return;
            }

            int numberOfPostsToSeed = 100;

            for (int i = 0; i < numberOfPostsToSeed; i++) {
                // Pick random prerequisites from the fetched lists
                UserEntity author = users.get(random.nextInt(users.size()));
                PropertyTypeEntity propertyType = propertyTypes.get(random.nextInt(propertyTypes.size())); // Pick a random available type
                ListingTypeEntity listingType = propertyType.getListingType(); // Get parent ListingType

                // Generate fake data
                String title = generatePostTitle(propertyType.getName(), listingType.getName());
                String description = faker.lorem().paragraphs(3).stream().reduce("", (a, b) -> a + "\n" + b);
                String address = faker.address().fullAddress();
                BigDecimal price = BigDecimal.valueOf(faker.number().randomDouble(0, 100000000, 1000000000));
                Double squareMeters = faker.number().randomDouble(2, 30, 500);
                PostStatus status = PostStatus.PUBLISHED; // Let's default seeded posts to PUBLISHED

                // Build Post Entity
                PostEntity post = new PostEntity();
                post.setTitle(title);
                post.setDescription(description);
                post.setAddress(address);
                post.setPrice(price);
                post.setSquareMeters(squareMeters);
                post.setStatus(status);
                post.setUser(author);
                post.setListingType(listingType);
                post.setPropertyType(propertyType);

                // Create and link Image Entities
                List<ImageEntity> images = new ArrayList<>();
                int numImages = random.nextInt(5) + 1; // 1 to 5 images per post
                for (int j = 0; j < numImages; j++) {
                    String imageUrl = SAMPLE_IMAGES.get(random.nextInt(SAMPLE_IMAGES.size()));
                    ImageEntity img = ImageEntity.builder()
                            .imageUrl(imageUrl)
                            .post(post)
                            .build();
                    images.add(img);
                }
                post.setImages(images); // Set the list on the post

                // Save Post (Cascade will save Images)
                postSpringDataRepository.save(post);
            }

            log.info("{} posts seeded successfully!", numberOfPostsToSeed);

        } else {
            log.info("Posts already exist, skipping seeding.");
        }
    }

    // Helper to generate a somewhat relevant title
    private String generatePostTitle(String propertyTypeName, String listingTypeName) {
        String action = listingTypeName.toLowerCase().contains("bán") ? "Bán" : "Cho thuê";
        String location = faker.address().cityName();
        return String.format("%s %s đẹp tại %s, giá tốt", action, propertyTypeName, location);
    }
}