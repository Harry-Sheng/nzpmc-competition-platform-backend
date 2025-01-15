package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.ChatGptRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGptService {
    @Value("${openai.api.key}")
    private String apiKey;

    private static final String openAiUrl = "https://api.openai.com/v1/chat/completions";

    public Map<String, Object> getChatGptResponse(ChatGptRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Add a developer message
        List<Map<String, String>> messages = new ArrayList<>(request.getMessages());
        Map<String, String> developerMessage = new HashMap<>();
        developerMessage.put("role", "system");
        developerMessage.put("content", "You are a NZPMC assistance bot, NZPMC is the new zealand physics and math competition platform");
        messages.add(0, developerMessage); // Add the developer message at the beginning

        // Prepare payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", request.getModel());
        payload.put("messages", messages);
        payload.put("store", request.isStore());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(openAiUrl, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with OpenAI API: " + e.getMessage(), e);
        }
    }
}
