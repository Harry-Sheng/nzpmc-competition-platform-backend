package com.nzpmc.CompetitionPlatform.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.nzpmc.CompetitionPlatform.models.Question.Difficulty;
import java.util.List;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {
    private String title;
    private List<String> options;
    private int correctChoiceIndex;
    private Difficulty difficulty;
    private List<String> topics;
}
