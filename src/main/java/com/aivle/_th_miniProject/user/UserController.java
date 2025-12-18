package com.aivle._th_miniProject.user;

import com.aivle._th_miniProject.book.dto.BookDetailResponse;
import com.aivle._th_miniProject.book.service.BookService;
import com.aivle._th_miniProject.order.dto.OrderResponse;
import com.aivle._th_miniProject.order.service.OrderService;
import com.aivle._th_miniProject.user.dtos.*;
import com.aivle._th_miniProject.user.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BookService bookService;
    private final OrderService orderService;

    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            User user = userService.signup(request);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(Map.of("errorMessage", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request){
        try {
            TokenResponse tokens = userService.reissue(request.getRefreshToken());
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("errorMessage", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout() {
        String email = SecurityUtil.getLoginEmail();
        try {
            userService.logout(email);
            return ResponseEntity.ok("로그아웃 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("errorMessage", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @GetMapping("/user/book/{userId}")
    public ResponseEntity<?> getBooks(@PathVariable Long userId) {
        try {
            List<BookDetailResponse> books = bookService.getBooksByUser(userId);
            return ResponseEntity.ok(books);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("errorMessage", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    @GetMapping("/user/order/{userId}")
    public ResponseEntity<?> getOrders(@PathVariable Long userId) {
        try {
            List<OrderResponse> response = orderService.getOrdersByUser(userId);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("errorMessage", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("errorMessage", e.getMessage()));
        }
    }

}
