package com.aivle._th_miniProject.book.dto;

import com.aivle._th_miniProject.book.entity.Book;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EntireBookResponse {
    private final List<BookItem> books;

    @Builder
    public EntireBookResponse(List<BookItem> books) {
        this.books = books;
    }

    public static EntireBookResponse from(List<Book> books) {
        List<BookItem> items = books.stream()
                .map(BookItem::new)
                .toList();
        return new EntireBookResponse(items);
    }

    @Getter
    public static class BookItem {
        private final Long bookId;
        private final String title;
        private final String description;
        private final String coverImage;
        private final String category;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        @Builder
        public BookItem(Book book) {
            this.bookId = book.getBookId();
            this.title = book.getTitle();
            this.description = book.getDescription();
            this.coverImage = book.getCoverImage();
            this.category = book.getCategory().getKoreanName();
            this.createdAt = book.getCreatedAt();
            this.updatedAt = book.getUpdatedAt();
        }
    }
}
