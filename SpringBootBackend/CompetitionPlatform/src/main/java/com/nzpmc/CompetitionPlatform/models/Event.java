package com.nzpmc.CompetitionPlatform.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Setter
@Getter
@Document("events") // Specifies the MongoDB collection name
public class Event {

    @Id
    @Field("_id")
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    private String description;

    private String competitionId;

    // Constructors
    public Event() {}

    public Event(String name, Date date, String description, String competitionId) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.competitionId = competitionId;
    }

}
