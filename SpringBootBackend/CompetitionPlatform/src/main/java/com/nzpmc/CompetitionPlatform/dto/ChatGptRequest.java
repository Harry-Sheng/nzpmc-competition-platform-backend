package com.nzpmc.CompetitionPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data // Auto generate getter and setters
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptRequest {
    private List<Map<String, String>> messages;
}
