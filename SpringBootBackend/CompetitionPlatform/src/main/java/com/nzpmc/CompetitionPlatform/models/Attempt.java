package com.nzpmc.CompetitionPlatform.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Setter
@Getter
@Document("attempt")
public class Attempt {

    @Id
    private String studentEmail;

    @NotNull(message = "Competition Id is required")
    private String competitionId;

    private Map<String, Integer> attempts; // Map<QuestionTitle, SelectedOptionIndex>

    // Constructors
    public Attempt() {}

    public Attempt(String studentEmail, String competitionId, Map<String, Integer> attempts) {
        this.studentEmail = studentEmail;
        this.competitionId = competitionId;
        this.attempts = attempts;
    }


}
