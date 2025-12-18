package com.aivle._th_miniProject.comment.service;

import com.aivle._th_miniProject.book.entity.Book;
import com.aivle._th_miniProject.book.repository.BookRepository;
import com.aivle._th_miniProject.comment.dto.CommentCreateRequest;
import com.aivle._th_miniProject.comment.dto.CommentCreateResponse;
import com.aivle._th_miniProject.comment.entity.Comment;
import com.aivle._th_miniProject.comment.repository.CommentRepository;
import com.aivle._th_miniProject.user.User;
import com.aivle._th_miniProject.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public CommentCreateResponse createComment(Long userId, Long bookId, CommentCreateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서를 찾을 수 없습니다."));
        Comment comment = Comment.builder()
                .user(user)
                .book(book)
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);

        return new CommentCreateResponse(
                saved.getCommentId(),
                saved.getUser().getId(),
                saved.getBook().getBookId(),
                saved.getDescription(),
                saved.getCreatedAt().toString()
        );
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId, String role) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        boolean isOwner = comment.getUser().getId().equals(userId);
        boolean isAdmin = role.equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new SecurityException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}