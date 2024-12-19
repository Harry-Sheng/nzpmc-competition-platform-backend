package com.nzpmc.CompetitionPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAttemptRequest {
    private String studentEmail;
    private Map<String, Integer> attempts; // Map<QuestionTitle, SelectedOptionIndex>
}
