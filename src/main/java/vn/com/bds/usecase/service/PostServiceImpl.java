package vn.com.bds.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.bds.domain.model.*; // Import domain models
import vn.com.bds.domain.repository.*; // Import repositories
import vn.com.bds.usecase.CreatePostUseCase;
import vn.com.bds.domain.exception.ResourceNotFoundException; // Import exception
import java.util.UUID; // <-- Import UUID

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements CreatePostUseCase {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ListingTypeRepository listingTypeRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    @Override
    public Post execute(CreatePostCommand command) {

        // 1. Find related entities
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + command.getUserId()));

        // --- CHANGE THESE LINES ---
        ListingType listingType = listingTypeRepository.findById(command.getListingTypeId()) // Use UUID
                .orElseThrow(() -> new ResourceNotFoundException("Listing type not found: " + command.getListingTypeId()));

        PropertyType propertyType = propertyTypeRepository.findById(command.getPropertyTypeId()) // Use UUID
                .orElseThrow(() -> new ResourceNotFoundException("Property type not found: " + command.getPropertyTypeId()));
        // --------------------------

        // 2. Build Post model
        Post post = Post.builder()
                .title(command.getTitle())
                .description(command.getDescription())
                .address(command.getAddress())
                .price(command.getPrice())
                .squareMeters(command.getSquareMeters())
                .status(PostStatus.PENDING_APPROVAL)
                .user(user)
                .listingType(listingType)
                .propertyType(propertyType)
                .build();

        // 3. Save Post
        return postRepository.save(post);
    }
}