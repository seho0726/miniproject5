package com.aivle._th_miniProject.user.dtos;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;
}
