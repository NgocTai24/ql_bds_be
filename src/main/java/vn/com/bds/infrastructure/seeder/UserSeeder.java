package vn.com.bds.infrastructure.seeder; // Or your package

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.com.bds.domain.model.Role;
import vn.com.bds.domain.model.User;
import vn.com.bds.domain.repository.UserRepository;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
// Remove unused imports for Normalizer and Pattern if you don't need generateEmail anymore

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker(new Locale("vi")); // Still useful for phone numbers
    private final Random random = new Random();

    private static final List<String> AVATARS = List.of(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmS6YQROniNylqAg3QWhl5S2A2DLP9USXCRA&s",
            "https://i.pinimg.com/564x/24/21/85/242185eaef43192fc3f9646932fe3b46.jpg",
            "https://uuc.edu.vn/uploads/blog/2025/01/16/92291b68d1c3a5391111046b5059de42139dec9e-1736979877.webp",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMDGGxnaZ5oCLmiT_iIWNAEuPiA5uhN4fgE4Q&s",
            "https://img.tripi.vn/cdn-cgi/image/width=700,height=700/https://gcs.tripi.vn/public-tripi/tripi-feed/img/483329swL/anh-mo-ta.png",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZbMPqYroYd4Hmfi_qj2Sxb4v7YBZeL6U07w&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIgjZTSL_kLHGL4_iIWNAEuPiA5uhN4fgE4Q&s"
    );

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            log.info("Seeding Users...");

            // 1. Create ADMIN (email can be @gmail.com if desired)
            User admin = User.builder()
                    .fullname("Quản Trị Viên")
                    .email("admin@gmail.com") // Changed to @gmail.com
                    .password(passwordEncoder.encode("123456")) // Keep admin password separate
                    .phone("0900000000")
                    .role(Role.ADMIN)
                    .avatarUrl(AVATARS.get(0))
                    .build();
            userRepository.save(admin);
            log.info("Admin user created.");

            // 2. Create 100 User/Agent with specific names and common password
            String commonPassword = passwordEncoder.encode("123456"); // Encode the common password
            int numberOfUsersToSeed = 100;

            for (int i = 1; i <= numberOfUsersToSeed; i++) {
                // --- MODIFIED SECTION ---
                String fullname = "user" + i; // Set specific fullname
                String email = "user" + i + "@gmail.com"; // Set specific email
                // --- END MODIFIED SECTION ---

                String phone = "09" + faker.number().digits(8); // Keep random phone
                Role role = random.nextBoolean() ? Role.AGENT : Role.USER; // Keep random role
                String avatarUrl = AVATARS.get(random.nextInt(AVATARS.size())); // Keep random avatar

                User commonUser = User.builder()
                        .fullname(fullname)
                        .email(email)
                        .password(commonPassword) // Use the common hashed password
                        .phone(phone)
                        .role(role)
                        .avatarUrl(avatarUrl)
                        .build();
                userRepository.save(commonUser);
            }
            log.info("{} users/agents (user1 to user{}) created.", numberOfUsersToSeed, numberOfUsersToSeed);
            log.info("Users seeded successfully!");
        } else {
            log.info("Users already exist, skipping user seeding.");
        }
    }
    private String generateEmail(String fullname, int index) {
        String temp = Normalizer.normalize(fullname.toLowerCase(), Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAccentName = pattern.matcher(temp).replaceAll("").replaceAll("đ", "d");
        String[] nameParts = noAccentName.split("\\s+");
        if (nameParts.length < 1) {
            return "user" + index + "@gmail.com";
        }
        String firstName = nameParts[nameParts.length - 1];
        String lastNameInitial = nameParts[0].length() > 0 ? String.valueOf(nameParts[0].charAt(0)) : "";
        return firstName + lastNameInitial + index + "@gmail.com";
    }
}