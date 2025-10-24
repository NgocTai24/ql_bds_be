package vn.com.bds.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.bds.domain.model.Post;
import vn.com.bds.presentation.dto.ApiResponse;
import vn.com.bds.presentation.dto.PostDto; // Import all DTOs
import vn.com.bds.usecase.ManagePostUseCase;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts") // Using /posts as base
@RequiredArgsConstructor
public class PostController {

    private final ManagePostUseCase managePostUseCase;

    // --- GET APIs ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDto>>> getAllPosts() {
        List<Post> models = managePostUseCase.getAllPosts();
        List<PostDto> dtos = models.stream().map(PostDto::fromModel).collect(Collectors.toList());
        return ApiResponse.success(dtos, "Posts fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDto>> getPostById(@PathVariable UUID id) {
        Post model = managePostUseCase.getPostById(id);
        // Call fromModel FIRST to get the DTO object
        PostDto dto = PostDto.fromModel(model);
        // Pass the DTO object to ApiResponse.success
        return ApiResponse.success(dto, "Post fetched successfully");
    }

    // --- POST API (Create) ---
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()") // Any logged-in user can create
    public ResponseEntity<ApiResponse<PostDto>> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            // Receive fields via @RequestParam for form-data
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("address") String address,
            @RequestParam("price") BigDecimal price,
            @RequestParam("squareMeters") Double squareMeters,
            @RequestParam("listingTypeId") UUID listingTypeId,
            @RequestParam("propertyTypeId") UUID propertyTypeId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {

        ManagePostUseCase.CreatePostCommand command = ManagePostUseCase.CreatePostCommand.builder()
                .title(title)
                .description(description)
                .address(address)
                .price(price)
                .squareMeters(squareMeters)
                .listingTypeId(listingTypeId)
                .propertyTypeId(propertyTypeId)
                .userEmail(userDetails.getUsername()) // Get email from token
                .images(images)
                .build();

        Post createdPost = managePostUseCase.createPost(command);
        // Call fromModel FIRST to get the DTO object
        PostDto dto = PostDto.fromModel(createdPost);
        // Pass the DTO object to ApiResponse.created
        return ApiResponse.created(dto, "Post created successfully");
    }

    // --- PUT API (Update) ---
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()") // Check ownership inside UseCase
    public ResponseEntity<ApiResponse<PostDto>> updatePost(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails,
            // Receive fields via @RequestParam
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("address") String address,
            @RequestParam("price") BigDecimal price,
            @RequestParam("squareMeters") Double squareMeters,
            @RequestParam("listingTypeId") UUID listingTypeId,
            @RequestParam("propertyTypeId") UUID propertyTypeId,
            @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages
            // @RequestParam(value = "imagesToDelete", required = false) List<UUID> imagesToDelete // Add if implementing delete
    ) throws IOException {

        ManagePostUseCase.UpdatePostCommand command = ManagePostUseCase.UpdatePostCommand.builder()
                .title(title)
                .description(description)
                .address(address)
                .price(price)
                .squareMeters(squareMeters)
                .listingTypeId(listingTypeId)
                .propertyTypeId(propertyTypeId)
                .userEmail(userDetails.getUsername())
                .newImages(newImages)
                // .imagesToDelete(imagesToDelete) // Add if implementing delete
                .build();

        Post updatedPost = managePostUseCase.updatePost(id, command);
        // Call fromModel FIRST to get the DTO object
        PostDto dto = PostDto.fromModel(updatedPost);
        // Pass the DTO object to ApiResponse.success
        return ApiResponse.success(dto, "Post updated successfully");
    }

    // --- DELETE API ---
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Check ownership/admin inside UseCase
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        managePostUseCase.deletePost(id, userDetails.getUsername());
        return ApiResponse.success("Post deleted successfully");
    }
}