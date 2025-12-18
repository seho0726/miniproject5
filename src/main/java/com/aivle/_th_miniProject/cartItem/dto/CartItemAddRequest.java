package com.aivle._th_miniProject.cartItem.dto;

import lombok.Getter;

@Getter
public class CartItemAddRequest {
    private Long bookId;
    private int quantity;
}
