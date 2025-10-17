package vn.com.bds.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.bds.domain.model.Post;
import vn.com.bds.presentation.dto.CreatePostRequest;
import vn.com.bds.presentation.dto.PostResponse;
import vn.com.bds.usecase.CreatePostUseCase;

@RestController
@RequestMapping("/api/v1/posts") // Đặt tiền tố chung cho API
@RequiredArgsConstructor
public class PostController {

    // Controller CHỈ inject các interface (cổng) từ USECASE
    private final CreatePostUseCase createPostUseCase;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest request) {

        // 1. Chuyển đổi DTO (API) sang Command (UseCase)
        CreatePostUseCase.CreatePostCommand command = CreatePostUseCase.CreatePostCommand.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .price(request.getPrice())
                .squareMeters(request.getSquareMeters())
                .userId(request.getUserId())
                .listingTypeId(request.getListingTypeId())
                .propertyTypeId(request.getPropertyTypeId())
                .build();

        // 2. Gọi UseCase để thực thi logic
        Post createdPost = createPostUseCase.execute(command);

        // 3. Chuyển đổi Model (Domain) sang DTO (Response)
        PostResponse response = PostResponse.fromModel(createdPost);

        // 4. Trả về 201 Created
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}