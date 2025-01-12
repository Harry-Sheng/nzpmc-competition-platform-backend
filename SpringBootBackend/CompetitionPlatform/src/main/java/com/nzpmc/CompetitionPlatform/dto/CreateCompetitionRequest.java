package com.nzpmc.CompetitionPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompetitionRequest {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
