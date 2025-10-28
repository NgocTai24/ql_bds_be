package vn.com.bds.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// Domain imports
import vn.com.bds.domain.exception.ResourceNotFoundException;
import vn.com.bds.domain.model.Comment;
import vn.com.bds.domain.model.Post;
import vn.com.bds.domain.model.Role;
import vn.com.bds.domain.model.User;
import vn.com.bds.domain.repository.CommentRepository;
import vn.com.bds.domain.repository.PostRepository;
import vn.com.bds.domain.repository.UserRepository;
// Infrastructure imports (needed for direct entity check)
import vn.com.bds.infrastructure.repository.datajpa.CommentSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.CommentEntity;
import vn.com.bds.infrastructure.repository.entity.PostEntity;
import vn.com.bds.infrastructure.repository.entity.UserEntity;
import vn.com.bds.infrastructure.repository.mapper.CommentMapper;
import vn.com.bds.infrastructure.repository.mapper.UserMapper; // Assuming UserMapper exists
import vn.com.bds.infrastructure.repository.mapper.PostMapper;   // Assuming PostMapper exists

// Usecase import
import vn.com.bds.usecase.ManageCommentUseCase;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceImpl implements ManageCommentUseCase {

    // Domain Repositories (Ports)
    private final CommentRepository commentRepository; // Use for saving domain model
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // Inject Spring Data Repo để dùng hàm lấy replies mới
    private final CommentSpringDataRepository commentSpringDataRepository;

    @Override @Transactional(readOnly = true)
    public List<Comment> getCommentsByPost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found: " + postId);
        }
        // 1. Lấy comment cấp 1 (đã bao gồm User và Post do @EntityGraph)
        List<Comment> topLevelComments = commentRepository.findByPostIdAndParentIsNull(postId);

        // 2. Với mỗi comment cấp 1, gọi hàm đệ quy để lấy replies
        topLevelComments.forEach(this::loadRepliesRecursively);

        return topLevelComments;
    }

    // --- HÀM ĐỆ QUY MỚI ---
    private void loadRepliesRecursively(Comment parentComment) {
        // Lấy các replies trực tiếp của parentComment bằng hàm mới trong Spring Data Repo
        List<CommentEntity> replyEntities = commentSpringDataRepository.findByParentIdOrderByCreatedAtAsc(parentComment.getId());
        if (replyEntities != null && !replyEntities.isEmpty()) {
            // Map các entities sang domain models
            List<Comment> replies = replyEntities.stream()
                    .map(CommentMapper::toDomain) // Mapper sẽ map User
                    .peek(reply -> reply.setPost(parentComment.getPost())) // Gán lại Post từ parent
                    .peek(reply -> reply.setParent(parentComment)) // Gán lại Parent
                    .collect(Collectors.toList());

            // Gán danh sách replies vào parentComment
            parentComment.setReplies(replies);

            // Tiếp tục gọi đệ quy cho từng reply để lấy các cấp sâu hơn
            replies.forEach(this::loadRepliesRecursively);
        } else {
            parentComment.setReplies(Collections.emptyList()); // Đặt list rỗng nếu không có reply
        }
    }
    @Override
    public Comment createComment(CreateCommentCommand command) {
        // 1. Fetch User and Post domain models (still needed for building the final Comment)
        User user = findUserByEmail(command.getUserEmail());
        Post post = findPostById(command.getPostId()); // Target post

        CommentEntity parentCommentEntity = null; // Use Entity for parent check
        Comment parentCommentModel = null; // Use Model for building the final Comment

        // 2. Handle Parent Comment (if replying)
        if (command.getParentId() != null) {
            // Fetch the PARENT Comment ENTITY directly
            parentCommentEntity = commentSpringDataRepository.findById(command.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Comment not found: " + command.getParentId()));

            // --- DIRECT CHECK USING ENTITY ---
            log.info("--- Replying to Comment ---");
            log.info("Target Post ID (from Request): {}", command.getPostId());
            log.info("Parent Comment ID (from Request): {}", command.getParentId());

            // Get the Post associated with the parent *entity*
            PostEntity parentPostEntity = parentCommentEntity.getPost();

            if (parentPostEntity != null) {
                log.info("Parent Comment's Post ID (from Parent Entity): {}", parentPostEntity.getId());
                log.info("Comparing Request Post ID [{}] with Parent's Post ID [{}]", command.getPostId(), parentPostEntity.getId());
                log.info("Result of equals(): {}", command.getPostId().equals(parentPostEntity.getId()));

                // THE CRITICAL CHECK using the parent entity's post ID
                if (!command.getPostId().equals(parentPostEntity.getId())) {
                    log.error("Validation Failed: Parent comment's post ID [{}] does not match target post ID [{}]", parentPostEntity.getId(), command.getPostId());
                    throw new IllegalArgumentException("Parent comment does not belong to the specified post.");
                }
            } else {
                // This should ideally not happen if DB constraints are set, but good to check
                log.error("Parent Comment Entity (ID: {}) has a NULL Post relationship!", parentCommentEntity.getId());
                throw new IllegalStateException("Parent comment is not associated with any post.");
            }
            // Map parent entity to model *after* the check for building the final comment
            parentCommentModel = CommentMapper.toDomain(parentCommentEntity);
            // --- END DIRECT CHECK ---
        }

        // 3. Build the NEW Comment domain model
        Comment newComment = Comment.builder()
                .content(command.getContent())
                .user(user)
                .post(post) // Link to the target Post model
                .parent(parentCommentModel) // Link to the parent Comment model (if it's a reply)
                .build();

        // 4. Save using the domain repository (which uses the mapper to convert back to entity)
        return commentRepository.save(newComment);
    }

    @Override
    public void deleteComment(DeleteCommentCommand command) {
        // ... (delete logic remains the same, using findCommentById helper) ...
    }

    // --- Helper Methods ---
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    private Post findPostById(UUID id) {
        // This might need adjustment if PostRepository.findById doesn't fetch everything needed later
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
    }

    private Comment findCommentById(UUID id) {
        // Use the domain repository here for consistency elsewhere, relies on mapper/EntityGraph setup
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found: " + id));
    }
}