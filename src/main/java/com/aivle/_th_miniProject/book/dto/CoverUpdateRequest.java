package com.aivle._th_miniProject.book.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoverUpdateRequest {
    @NotBlank(message = "유효한 이미지가 아닙니다.")
    private String coverImage;
}
