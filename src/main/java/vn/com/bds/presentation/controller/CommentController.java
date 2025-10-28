package vn.com.bds.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.com.bds.domain.model.Comment;
import vn.com.bds.presentation.dto.ApiResponse;
import vn.com.bds.presentation.dto.CommentDto;
import vn.com.bds.presentation.dto.CreateCommentRequest;
import vn.com.bds.usecase.ManageCommentUseCase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments") // Base path for comments
@RequiredArgsConstructor
public class CommentController {

    private final ManageCommentUseCase manageCommentUseCase;

    // Get comments for a specific post
    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDto>>> getCommentsForPost(@PathVariable UUID postId) {
        List<Comment> models = manageCommentUseCase.getCommentsByPost(postId);
        List<CommentDto> dtos = models.stream().map(CommentDto::fromModel).collect(Collectors.toList());
        return ApiResponse.success(dtos, "Comments fetched successfully for post");
    }

    // Create a new comment (or reply)
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Any logged-in user can comment
    public ResponseEntity<ApiResponse<CommentDto>> createComment(
            @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        ManageCommentUseCase.CreateCommentCommand command = ManageCommentUseCase.CreateCommentCommand.builder()
                .content(request.getContent())
                .postId(request.getPostId())
                .parentId(request.getParentId()) // Will be null if not a reply
                .userEmail(userDetails.getUsername())
                .build();

        Comment createdComment = manageCommentUseCase.createComment(command);
        return ApiResponse.created(CommentDto.fromModel(createdComment), "Comment created successfully");
    }

    // Delete a comment
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()") // Authorization check done in UseCase
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable UUID commentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        ManageCommentUseCase.DeleteCommentCommand command = ManageCommentUseCase.DeleteCommentCommand.builder()
                .commentId(commentId)
                .userEmail(userDetails.getUsername())
                .build();

        manageCommentUseCase.deleteComment(command);
        return ApiResponse.success("Comment deleted successfully");
    }
}