package com.aivle._th_miniProject.user.dtos;


import com.aivle._th_miniProject.user.User;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private User.Role role;
}
