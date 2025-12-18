package com.aivle._th_miniProject.order.dto;

import com.aivle._th_miniProject.order.entity.Order;
import lombok.*;
import java.util.List;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private Integer totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> items;

    @Getter
    @Builder
    public static class OrderItemResponse {
        private Long bookId;
        private String title;
        private Integer quantity;
        private Integer price;
    }

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems().stream()
                        .map(i -> OrderItemResponse.builder()
                                .bookId(i.getBook().getBookId())
                                .title(i.getBook().getTitle())
                                .quantity(i.getQuantity())
                                .price(i.getPrice())
                                .build()
                        ).toList())
                .build();
    }
}
