package com.nzpmc.CompetitionPlatform.dto;

import lombok.*;

import java.util.Date;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    private String name;
    private String description;
    private Date date;
}

