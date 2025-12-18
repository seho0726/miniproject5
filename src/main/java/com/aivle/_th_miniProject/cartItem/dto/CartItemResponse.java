package com.aivle._th_miniProject.cartItem.dto;

import com.aivle._th_miniProject.cartItem.entity.CartItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {

    private Long cartItemId;
    private Long bookId;
    private String title;
    private String coverImage;
    private int quantity;
    private int stock;
    private int price;

    public static CartItemResponse from(CartItem item) {
        return CartItemResponse.builder()
                .cartItemId(item.getId())
                .bookId(item.getBook().getBookId())
                .title(item.getBook().getTitle())
                .coverImage(item.getBook().getCoverImage())
                .quantity(item.getQuantity())
                .stock(item.getBook().getStock())
                .price(item.getBook().getPrice().intValue())
                .build();
    }

}
