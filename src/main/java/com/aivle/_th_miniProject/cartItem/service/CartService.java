package com.aivle._th_miniProject.cartItem.service;

import com.aivle._th_miniProject.book.entity.Book;
import com.aivle._th_miniProject.book.repository.BookRepository;
import com.aivle._th_miniProject.cartItem.dto.*;
import com.aivle._th_miniProject.cartItem.entity.CartItem;
import com.aivle._th_miniProject.cartItem.repository.CartItemRepository;
import com.aivle._th_miniProject.user.User;
import com.aivle._th_miniProject.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    @Transactional
    public void addToCart(Long userId, CartItemAddRequest req) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Book book = bookRepo.findById(req.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다."));

        CartItem exist = cartRepo.findByUserAndBook(user, book).orElse(null);

        if (exist != null) {
            exist.increaseQuantity(req.getQuantity());
            return;
        }

        cartRepo.save(
                CartItem.builder()
                        .user(user)
                        .book(book)
                        .quantity(req.getQuantity())
                        .build()
        );
    }

    public CartAllResponse getCart(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<CartItem> list = cartRepo.findByUserOrderByCreatedAtDesc(user);

        List<CartItemResponse> items =
                list.stream().map(CartItemResponse::from).toList();

        return CartAllResponse.from(items);
    }

    @Transactional
    public void updateQuantity(Long cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        CartItem item = cartRepo.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다."));

        item.updateQuantity(quantity);
    }

    @Transactional
    public void removeItem(Long cartItemId) {
        cartRepo.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<CartItem> items = cartRepo.findByUserOrderByCreatedAtDesc(user);
        cartRepo.deleteAll(items);
    }
}