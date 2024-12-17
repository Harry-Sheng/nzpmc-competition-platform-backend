package com.nzpmc.CompetitionPlatform.dto;

import lombok.*;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    private String name;
    private String email;
    private String password;
}
