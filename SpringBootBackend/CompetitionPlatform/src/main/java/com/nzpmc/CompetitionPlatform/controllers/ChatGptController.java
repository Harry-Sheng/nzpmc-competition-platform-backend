package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.ChatGptService;
import com.nzpmc.CompetitionPlatform.dto.ChatGptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chatgpt")
public class ChatGptController {
    private final ChatGptService chatGPTService;

    @Autowired
    public ChatGptController(ChatGptService chatGPTService){
        this.chatGPTService = chatGPTService;
    }

    @PostMapping("/chat")
    public Map<String, Object> getChatGptResponse(@RequestBody ChatGptRequest request) {
        return chatGPTService.getChatGptResponse(request);
    }
}
