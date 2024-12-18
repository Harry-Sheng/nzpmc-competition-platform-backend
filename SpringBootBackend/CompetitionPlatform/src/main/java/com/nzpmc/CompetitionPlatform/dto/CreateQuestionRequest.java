package com.nzpmc.CompetitionPlatform.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {
    private String title;
    private List<String> options;
    private int correctChoiceIndex;
}
