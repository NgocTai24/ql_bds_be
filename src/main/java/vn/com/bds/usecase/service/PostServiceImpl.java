package vn.com.bds.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.bds.domain.model.ListingType;
import vn.com.bds.domain.model.Post;
import vn.com.bds.domain.model.PostStatus;
import vn.com.bds.domain.model.PropertyType;
import vn.com.bds.domain.model.User;
import vn.com.bds.domain.repository.ListingTypeRepository;
import vn.com.bds.domain.repository.PostRepository;
import vn.com.bds.domain.repository.PropertyTypeRepository;
import vn.com.bds.domain.repository.UserRepository;
import vn.com.bds.usecase.CreatePostUseCase;

@Service // <-- ĐÂY LÀ DÒNG QUAN TRỌNG NHẤT
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements CreatePostUseCase {

    // Inject các "cổng" (interface) từ DOMAIN
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ListingTypeRepository listingTypeRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    @Override
    public Post execute(CreatePostCommand command) {
        // --- Đây là nơi chứa logic nghiệp vụ ---

        // 1. Kiểm tra sự tồn tại của các đối tượng liên quan
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListingType listingType = listingTypeRepository.findById(command.getListingTypeId())
                .orElseThrow(() -> new RuntimeException("Listing type not found"));

        PropertyType propertyType = propertyTypeRepository.findById(command.getPropertyTypeId())
                .orElseThrow(() -> new RuntimeException("Property type not found"));

        // 2. Xây dựng đối tượng Post (Domain Model)
        Post post = Post.builder()
                .title(command.getTitle())
                .description(command.getDescription())
                .address(command.getAddress())
                .price(command.getPrice())
                .squareMeters(command.getSquareMeters())
                .status(PostStatus.PENDING_APPROVAL) // Logic: bài mới luôn phải chờ duyệt
                .user(user)
                .listingType(listingType)
                .propertyType(propertyType)
                .build();

        // 3. Dùng "cổng" repository để lưu
        return postRepository.save(post);
    }
}