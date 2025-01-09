package com.nzpmc.CompetitionPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class AddQuestionToCompetitionRequest {
    private String title;
}
