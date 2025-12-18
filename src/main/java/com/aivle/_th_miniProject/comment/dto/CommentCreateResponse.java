package com.aivle._th_miniProject.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateResponse {

    private Long commentId;
    private Long userId;
    private Long bookId;
    private String description;
    private String createdAt;
}
