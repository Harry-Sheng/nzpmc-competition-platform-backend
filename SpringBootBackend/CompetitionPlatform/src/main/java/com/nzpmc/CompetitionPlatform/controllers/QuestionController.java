package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.QuestionService;
import com.nzpmc.CompetitionPlatform.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // get question
    @GetMapping
    public ResponseEntity<Object> getQuestions(@RequestHeader(value = "Authorization") String authorizationHeader) {
        List<Question> questions = questionService.getQuestions(authorizationHeader);
        return ResponseEntity.ok(questions);
    }
}
