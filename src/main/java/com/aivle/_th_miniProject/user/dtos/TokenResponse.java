package com.aivle._th_miniProject.user.dtos;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
