package com.nzpmc.CompetitionPlatform.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Document("competition")
public class Competition {

    @Id
    private String title;

    private List<String> questionIds;

    // Constructors
    public Competition() {}

    public Competition(String title, List<String> questionIds) {
        this.title = title;
        this.questionIds = questionIds;
    }

}
