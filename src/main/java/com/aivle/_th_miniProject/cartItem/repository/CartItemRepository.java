package com.aivle._th_miniProject.cartItem.repository;

import com.aivle._th_miniProject.cartItem.entity.CartItem;
import com.aivle._th_miniProject.user.User;
import com.aivle._th_miniProject.book.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserOrderByCreatedAtDesc(User user);

    Optional<CartItem> findByUserAndBook(User user, Book book);
}