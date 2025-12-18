package com.aivle._th_miniProject.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "내용이 없는 댓글은 작성 불가합니다.")
    private String description;
}