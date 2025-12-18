package com.aivle._th_miniProject.order.controller;

import com.aivle._th_miniProject.order.dto.OrderCreateRequest;
import com.aivle._th_miniProject.order.dto.OrderResponse;
import com.aivle._th_miniProject.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    // 결제 처리
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<?> payOrder(@PathVariable Long orderId) {
        try {
            OrderResponse response = orderService.payOrder(orderId);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("취소된 주문")) {
                return ResponseEntity.status(400).body(Map.of("errorMessage", e.getMessage()));
            } else if (e.getMessage().contains("결제 완료된 주문")) {
                return ResponseEntity.status(400).body(Map.of("errorMessage", e.getMessage()));
            } else {
                return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    // 주문 취소
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            OrderResponse response = orderService.cancelOrder(orderId);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("취소된 주문")) {
                return ResponseEntity.status(400).body(Map.of("errorMessage", e.getMessage()));
            } else if (e.getMessage().contains("결제 완료된 주문")) {
                return ResponseEntity.status(400).body(Map.of("errorMessage", e.getMessage()));
            } else {
                return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("errorMessage", e.getMessage()));
        }
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        try {
            OrderResponse response = orderService.getOrderDetail(orderId);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("errorMessage", e.getMessage()));
        }
    }
}
