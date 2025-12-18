package com.aivle._th_miniProject.book.dto;

import com.aivle._th_miniProject.book.entity.Book;
import com.aivle._th_miniProject.book.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class BookCreateResponse {

    private Long bookId;
    private String title;
    private String author;
    private String description;
    private String coverImage;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer stock;
    private Integer price;

    public static BookCreateResponse from(Book book) {
        return BookCreateResponse.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .coverImage(book.getCoverImage())
                .category(book.getCategory())
                .stock(book.getStock())
                .price(book.getPrice())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
