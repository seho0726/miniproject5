package com.aivle._th_miniProject.cartItem.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class CartAllResponse {
    private List<CartItemResponse> items;
    private int totalQuantity;
    private int totalPrice;

    public static CartAllResponse from(List<CartItemResponse> items) {
        int totalQuantity = items.stream().mapToInt(CartItemResponse::getQuantity).sum();
        int totalPrice = items.stream().mapToInt(i -> i.getQuantity() * i.getPrice()).sum();

        return CartAllResponse.builder()
                .items(items)
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .build();
    }
}
