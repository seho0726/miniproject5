package com.aivle._th_miniProject.cartItem.controller;

import com.aivle._th_miniProject.cartItem.dto.*;
import com.aivle._th_miniProject.cartItem.service.CartService;
import com.aivle._th_miniProject.user.User;
import com.aivle._th_miniProject.user.UserRepository;
import com.aivle._th_miniProject.user.jwt.SecurityUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    private Long getLoginUserId() {
        String email = SecurityUtil.getLoginEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartItemAddRequest request) {

        try {
            cartService.addToCart(getLoginUserId(), request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "장바구니에 담았습니다."));

        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("찾을 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }

            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getCart() {

        try {
            CartAllResponse response = cartService.getCart(getLoginUserId());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("찾을 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }

            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long cartItemId,
            @RequestBody UpdateQuantityRequest request
    ) {

        try {
            cartService.updateQuantity(cartItemId, request.getQuantity());
            return ResponseEntity.ok(Map.of("message", "수량이 변경되었습니다."));

        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("찾을 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }

            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) {

        try {
            cartService.removeItem(cartItemId);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("찾을 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }

            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart() {

        try {
            cartService.clearCart(getLoginUserId());
            return ResponseEntity.ok(Map.of("message", "장바구니를 비웠습니다."));

        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("찾을 수 없습니다")) {
                return ResponseEntity.status(404)
                        .body(Map.of("errorMessage", e.getMessage()));
            }

            return ResponseEntity.status(400)
                    .body(Map.of("errorMessage", e.getMessage()));
        }
    }
}
