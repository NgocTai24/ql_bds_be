package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile; // For file input
import vn.com.bds.domain.model.Post;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ManagePostUseCase {
    // Read
    List<Post> getAllPosts();
    Post getPostById(UUID id);

    // Write
    Post createPost(CreatePostCommand command);
    Post updatePost(UUID id, UpdatePostCommand command);
    void deletePost(UUID id, String currentUserEmail); // Pass user email for authorization check

    // Command for Creation (includes files)
    @Value @Builder
    class CreatePostCommand {
        String title;
        String description;
        String address;
        BigDecimal price;
        Double squareMeters;
        UUID propertyTypeId;
        String userEmail; // Email of the creator (from token)
        List<MultipartFile> images; // List of image files
    }

    // Command for Update (includes files, potentially optional fields)
    @Value @Builder
    class UpdatePostCommand {
        String title;
        String description;
        String address;
        BigDecimal price;
        Double squareMeters;
        UUID propertyTypeId;
        String userEmail; // Email of the user updating (from token)
        List<MultipartFile> newImages; // List of *new* image files to add
    }
}