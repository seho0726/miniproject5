package com.aivle._th_miniProject.book.controller;

import com.aivle._th_miniProject.book.dto.*;
import com.aivle._th_miniProject.book.entity.Book;
import com.aivle._th_miniProject.book.service.BookService;
import com.aivle._th_miniProject.user.User;
import com.aivle._th_miniProject.user.UserRepository;
import com.aivle._th_miniProject.user.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
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

    @PostMapping
    public ResponseEntity<?> createBook(
            @Validated @RequestBody BookCreateRequest request
    ){
        Long userId = getLoginUserId();

        try {
            BookCreateResponse response = bookService.createBook(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            //메시지로 404 / 400 구분
            if (e.getMessage().contains("찾을 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }

            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getEntireBooks()
    {
        try {
            EntireBookResponse response = bookService.getAllBooks();
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookDetail(
            @PathVariable Long bookId
    ) {
        try {
            BookDetailResponse response = bookService.getBookDetail(bookId);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("도서 정보를 가져올 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }
            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(
            @PathVariable Long bookId,
            @Validated @RequestBody BookUpdateRequest request
    ) {
        Long userId = getLoginUserId();
        String role = getLoginUserRole();

        try {
            BookDetailResponse response = bookService.updateBook(bookId, request, userId, role);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("요청한 도서를 찾을 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }
                return ResponseEntity.status(403)
                        .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(
            @PathVariable Long bookId
    ) {
        Long userId = getLoginUserId();
        String role = getLoginUserRole();

        try {
            bookService.deleteBook(bookId, userId, role);
            return ResponseEntity.noContent().build(); //204
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("도서 정보를 가져올 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }
                return ResponseEntity.status(403)
                        .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PatchMapping("/{bookId}")
    public ResponseEntity<?> updateCover(
            @PathVariable Long bookId,
            @Validated @RequestBody CoverUpdateRequest request
    ) {
        Long userId = getLoginUserId();
        String role = getLoginUserRole();

        try {
            CoverUpdateResponse response =
                    bookService.updateCover(bookId, userId, role, request.getCoverImage());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("도서 정보를 가져올 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }

            if (e.getMessage().contains("본인의 표지만")) {
                return ResponseEntity.status(403)
                        .body(Map.of("errorMessage", e.getMessage()));
            }
            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PutMapping("/{bookId}/stock")
    public ResponseEntity<?> updateStock(
            @PathVariable Long bookId,
            @Validated @RequestBody StockUpdateRequest request) {
        System.out.println("bookId = " + bookId);
        System.out.println("request = " + request);
        try {
            StockUpdateResponse response = bookService.updateStock(bookId, request);
            return ResponseEntity.ok().body(response);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(404)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(403)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }
}
