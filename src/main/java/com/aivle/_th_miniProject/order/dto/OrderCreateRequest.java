package com.aivle._th_miniProject.order.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class OrderCreateRequest {
    private Long userId;
    private List<OrderItemRequest> items;

    @Getter
    public static class OrderItemRequest {
        private Long bookId;
        private Integer quantity;
    }
}
