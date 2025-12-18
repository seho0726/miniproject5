package com.aivle._th_miniProject.comment.controller;

import com.aivle._th_miniProject.comment.dto.CommentCreateRequest;
import com.aivle._th_miniProject.comment.dto.CommentCreateResponse;
import com.aivle._th_miniProject.comment.service.CommentService;
import com.aivle._th_miniProject.user.User;
import com.aivle._th_miniProject.user.UserRepository;
import com.aivle._th_miniProject.user.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    private Long getLoginUserId() {
        String email = SecurityUtil.getLoginEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getId();
    }

    private String getLoginUserRole() {
        String email = SecurityUtil.getLoginEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getRole().name();
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<CommentCreateResponse> createComment(
            @PathVariable Long bookId,
            @RequestBody CommentCreateRequest request
    ) {
        Long userId = getLoginUserId();
        CommentCreateResponse response = commentService.createComment(userId, bookId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId
    ) {
        Long userId = getLoginUserId();
        String role = getLoginUserRole();

        try {
            commentService.deleteComment(commentId, userId, role);
            return ResponseEntity.noContent().build();

        }catch (IllegalArgumentException e) {
            if (e.getMessage().contains("찾을 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }

            return ResponseEntity.status(403)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

}