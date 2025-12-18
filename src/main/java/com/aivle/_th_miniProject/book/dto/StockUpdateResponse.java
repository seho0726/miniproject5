package com.aivle._th_miniProject.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StockUpdateResponse {
    private Long bookId;
    private String title;
    private Integer stock;
}
