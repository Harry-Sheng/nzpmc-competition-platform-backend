package com.nzpmc.CompetitionPlatform.dto;

import lombok.*;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String name;
    private String role;
}
