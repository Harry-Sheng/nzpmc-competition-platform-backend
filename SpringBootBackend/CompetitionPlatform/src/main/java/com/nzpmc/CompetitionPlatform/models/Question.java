package com.nzpmc.CompetitionPlatform.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Document("question")
public class Question {

    @Id
    private String title;

    private List<String> options;

    private int correctChoiceIndex;

    // Constructors
    public Question() {}

    public Question(String title, List<String> options, int correctChoiceIndex) {
        this.title = title;
        this.options = options;
        this.correctChoiceIndex = correctChoiceIndex;
    }

}
