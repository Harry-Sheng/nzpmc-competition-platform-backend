package com.nzpmc.CompetitionPlatform.dto;

import lombok.*;
import java.util.List;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class FilterQuestionRequest {
    private String difficulty;
    private List<String> topics;
}

