package vn.com.bds.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException; // For authorization check
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.com.bds.domain.exception.ResourceNotFoundException;
import vn.com.bds.domain.model.*; // Import all models
import vn.com.bds.domain.repository.*; // Import all repositories
// Import Entities and Mappers needed for cascade
import vn.com.bds.infrastructure.repository.entity.ImageEntity;
import vn.com.bds.infrastructure.repository.entity.PostEntity;
import vn.com.bds.infrastructure.repository.entity.ListingTypeEntity;
import vn.com.bds.infrastructure.repository.entity.PropertyTypeEntity;
import vn.com.bds.infrastructure.repository.mapper.ImageMapper;
import vn.com.bds.infrastructure.repository.mapper.PostMapper;
import vn.com.bds.infrastructure.repository.mapper.ListingTypeMapper;
import vn.com.bds.infrastructure.repository.mapper.PropertyTypeMapper;
// Import Spring Data JPA Repo for Post to save PostEntity directly
import vn.com.bds.infrastructure.repository.datajpa.PostSpringDataRepository;
import vn.com.bds.usecase.ManagePostUseCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostServiceImpl implements ManagePostUseCase {

    // Domain Repositories (Ports)
    private final PostRepository postRepository; // Still useful for reads, delete
    private final UserRepository userRepository;
    private final ListingTypeRepository listingTypeRepository;
    private final PropertyTypeRepository propertyTypeRepository;
    private final ImageStorageRepository imageStorageRepository; // For uploading

    // Infrastructure Spring Data Repository (for cascade saving)
    private final PostSpringDataRepository postSpringDataRepository;
    // We no longer need ImageRepository injected here if using cascade
    // private final ImageRepository imageRepository;

    @Override @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override @Transactional(readOnly = true)
    public Post getPostById(UUID id) {
        // Fetch PostEntity including images for mapping back
        PostEntity postEntity = postSpringDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        // Map including images now
        Post post = PostMapper.toDomain(postEntity);
        if (postEntity.getImages() != null) {
            post.setImages(postEntity.getImages().stream().map(ImageMapper::toDomain).collect(Collectors.toList()));
        }
        return post;
    }

    @Override
    public Post createPost(CreatePostCommand command) {
        // 1. Find related domain models (for validation/linking)
        User user = findUserByEmail(command.getUserEmail());
        ListingType listingType = findListingTypeById(command.getListingTypeId());
        PropertyType propertyType = findPropertyTypeById(command.getPropertyTypeId());

        // 2. Build Post DOMAIN MODEL (optional but can be good practice)
        Post postModel = Post.builder()
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

        // 3. Map to Post ENTITY
        PostEntity postEntityToSave = PostMapper.toEntity(postModel);
        // Ensure relationships are set on the entity side
        // PostMapper.toEntity should already handle User, ListingType, PropertyType Entities

        // 4. Upload images and create/link Image ENTITIES
        // This helper now returns List<ImageEntity>
        List<ImageEntity> imageEntities = uploadAndCreateImageEntities(command.getImages(), postEntityToSave);

        // 5. Add linked ImageEntities TO THE PostEntity's list
        postEntityToSave.setImages(imageEntities); // This is crucial for cascade

        // 6. Save the PostEntity (CASCADE will save ImageEntities)
        PostEntity savedPostEntity = postSpringDataRepository.save(postEntityToSave);

        // 7. Map the fully saved entity back to a domain model to return
        // We need to map images back as well
        Post savedPost = PostMapper.toDomain(savedPostEntity);
        if (savedPostEntity.getImages() != null) {
            savedPost.setImages(savedPostEntity.getImages().stream().map(ImageMapper::toDomain).collect(Collectors.toList()));
        }
        return savedPost;
    }

    @Override
    public Post updatePost(UUID id, UpdatePostCommand command) {
        // 1. Find existing Post ENTITY to update and verify ownership
        PostEntity existingPostEntity = postSpringDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));

        if (!existingPostEntity.getUser().getEmail().equals(command.getUserEmail())) {
            // Add Admin role check here if needed
            throw new AccessDeniedException("User not authorized to update this post");
        }

        // 2. Find related entities if they are being changed
        ListingTypeEntity listingTypeEntity = ListingTypeMapper.toEntity(findListingTypeById(command.getListingTypeId()));
        PropertyTypeEntity propertyTypeEntity = PropertyTypeMapper.toEntity(findPropertyTypeById(command.getPropertyTypeId()));

        // 3. Update fields on the ENTITY
        existingPostEntity.setTitle(command.getTitle());
        existingPostEntity.setDescription(command.getDescription());
        existingPostEntity.setAddress(command.getAddress());
        existingPostEntity.setPrice(command.getPrice());
        existingPostEntity.setSquareMeters(command.getSquareMeters());
        existingPostEntity.setListingType(listingTypeEntity);
        existingPostEntity.setPropertyType(propertyTypeEntity);
        // existingPostEntity.setStatus(...) // If status needs update

        // 4. Handle image updates
        if (command.getNewImages() != null && !command.getNewImages().isEmpty()) {
            // Upload new images and link them TO THE EXISTING PostEntity
            List<ImageEntity> addedImageEntities = uploadAndCreateImageEntities(command.getNewImages(), existingPostEntity);
            if (existingPostEntity.getImages() == null) {
                existingPostEntity.setImages(new ArrayList<>());
            }
            // Add the NEW entities to the list managed by JPA
            existingPostEntity.getImages().addAll(addedImageEntities);
        }
        // --- TODO: Add logic for deleting images ---
        // If implementing delete:
        // 1. Find ImageEntities by IDs from command.getImagesToDelete()
        // 2. Remove them from existingPostEntity.getImages() list (orphanRemoval=true will delete from DB)
        // 3. Call imageStorageRepository.delete() for each removed image URL

        // 5. Save the updated PostEntity (CASCADE handles new/removed images)
        PostEntity updatedPostEntity = postSpringDataRepository.save(existingPostEntity);

        // 6. Map back to domain model
        Post updatedPost = PostMapper.toDomain(updatedPostEntity);
        if(updatedPostEntity.getImages() != null){
            updatedPost.setImages(updatedPostEntity.getImages().stream().map(ImageMapper::toDomain).collect(Collectors.toList()));
        }
        return updatedPost;
    }

    @Override
    public void deletePost(UUID id, String currentUserEmail) {
        // Fetch PostEntity to check ownership and get image URLs
        PostEntity postToDelete = postSpringDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));

        // Authorization Check
        if (!postToDelete.getUser().getEmail().equals(currentUserEmail)) {
            // Add Admin role check here if needed
            throw new AccessDeniedException("User not authorized to delete this post");
        }

        // Delete images from Cloudinary first
        if (postToDelete.getImages() != null) {
            postToDelete.getImages().forEach(imageEntity -> {
                try {
                    // Make sure ImageEntity has getImageUrl()
                    imageStorageRepository.delete(imageEntity.getImageUrl());
                } catch (Exception e) {
                    log.error("Failed to delete image from storage: {}", imageEntity.getImageUrl(), e);
                }
            });
        }

        // Delete post from DB using Spring Data repo (Cascade + orphanRemoval handles images in DB)
        postSpringDataRepository.deleteById(id);
    }

    // --- Helper Methods ---

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User (Author) not found: " + email));
    }

    private ListingType findListingTypeById(UUID id) {
        return listingTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing type not found: " + id));
    }

    private PropertyType findPropertyTypeById(UUID id) {
        return propertyTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property type not found: " + id));
    }

    /**
     * Helper to upload images to storage and create linked ImageEntity objects.
     * DOES NOT SAVE the ImageEntities directly.
     *
     * @param files      List of files from the request.
     * @param postEntity The parent PostEntity to link the images to.
     * @return List of newly created ImageEntity objects (linked but not yet saved).
     */
    private List<ImageEntity> uploadAndCreateImageEntities(List<MultipartFile> files, PostEntity postEntity) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<ImageEntity> imageEntities = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 1. Upload to Cloudinary
                    String imageUrl = imageStorageRepository.upload(file.getBytes(), file.getOriginalFilename());

                    // 2. Create Image ENTITY and link it back to the Post ENTITY
                    ImageEntity imageEntity = ImageEntity.builder()
                            .imageUrl(imageUrl)
                            .post(postEntity) // <-- Link to parent PostEntity
                            .build();

                    imageEntities.add(imageEntity); // Add to list

                } catch (IOException e) {
                    log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                    throw new RuntimeException("Failed to upload image: " + file.getOriginalFilename(), e);
                }
            }
        }
        // Return the list of entities; DO NOT SAVE them here.
        return imageEntities;
    }
}