package com.aivle._th_miniProject.user.dtos;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SignupRequest {
    private String name;
    private String email;
    private String password;
}
