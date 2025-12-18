package com.aivle._th_miniProject.book.repository;

import com.aivle._th_miniProject.book.entity.Book;
import com.aivle._th_miniProject.book.entity.Category;
import com.aivle._th_miniProject.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByUser(User user);

    List<Book> findByCategory(Category category);

    List<Book> findByTitleContaining(String keyword);

    List<Book> findAllByOrderByUpdatedAtDesc();
}
