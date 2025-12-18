package com.aivle._th_miniProject.book.service;

import com.aivle._th_miniProject.book.dto.*;
import com.aivle._th_miniProject.book.entity.Book;
import com.aivle._th_miniProject.book.repository.BookRepository;
import com.aivle._th_miniProject.user.User;
import com.aivle._th_miniProject.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookCreateResponse createBook(BookCreateRequest request, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Book book = Book.builder()
                .user(user)
                .title(request.getTitle())
                .author(user.getName())
                .description(request.getDescription())
                .coverImage(request.getCoverImage())
                .category(request.getCategory())
                .stock(request.getStock())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Book saved = bookRepository.save(book);

        return BookCreateResponse.from(saved);
    }

    @Transactional
    public BookDetailResponse getBookDetail(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("도서 정보를 가져올 수 없습니다.")
                );

        book.setViewCount(book.getViewCount() + 1);

        return BookDetailResponse.from(book);
    }

    public EntireBookResponse getAllBooks() {
        List<Book> books = bookRepository.findAllByOrderByUpdatedAtDesc();
        return EntireBookResponse.from(books);
    }

    @Transactional
    public BookDetailResponse updateBook(Long bookId, BookUpdateRequest request, Long userId, String role) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("요청한 도서를 찾을 수 없습니다.")
                );

        // 권한 체크
        boolean isOwner = book.getUser().getId().equals(userId);
        boolean isAdmin = role.equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("본인의 도서만 수정할 수 있습니다.");
        }

        book.setTitle(request.getTitle());
        book.setDescription(request.getDescription());
        book.setCoverImage(request.getCoverImage());
        book.setCategory(request.getCategory());
        book.setStock(request.getStock());
        book.setPrice(request.getPrice());
        book.setUpdatedAt(LocalDateTime.now());

        return BookDetailResponse.from(book);
    }

    @Transactional
    public void deleteBook(Long bookId, Long userId, String role) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("도서 정보를 가져올 수 없습니다.")
                );

        boolean isOwner = book.getUser().getId().equals(userId);
        boolean isAdmin = role.equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("본인의 도서만 삭제할 수 있습니다.");
        }

        bookRepository.delete(book);
    }

    @Transactional
    public CoverUpdateResponse updateCover(Long bookId, Long userId, String role, String newCover) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("도서 정보를 가져올 수 없습니다.")
                );

        boolean isOwner = book.getUser().getId().equals(userId);
        boolean isAdmin = role.equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("본인의 표지만 수정할 수 있습니다.");
        }

        book.setCoverImage(newCover);
        book.setUpdatedAt(LocalDateTime.now());

        return new CoverUpdateResponse(
                book.getBookId(),
                book.getCoverImage(),
                book.getUpdatedAt()
        );
    }

    public List<BookDetailResponse> getBooksByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        List<Book> books = bookRepository.findByUser(user);

        return books.stream()
                .map(BookDetailResponse::from)
                .toList();
    }

    @Transactional
    public StockUpdateResponse updateStock(Long bookId, StockUpdateRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));
        if(request.getStock() < 0) {
            throw new IllegalArgumentException("재고는 음수일 수 없습니다.");
        }
        book.setStock(request.getStock());
        bookRepository.save(book);
        return new StockUpdateResponse(
                book.getBookId(),
                book.getTitle(),
                book.getStock());
    }
}
