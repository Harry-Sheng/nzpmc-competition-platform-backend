package com.nzpmc.CompetitionPlatform.dto;

import lombok.*;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}

