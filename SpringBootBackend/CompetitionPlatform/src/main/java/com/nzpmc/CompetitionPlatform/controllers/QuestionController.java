package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.QuestionService;
import com.nzpmc.CompetitionPlatform.dto.CreateQuestionRequest;
import com.nzpmc.CompetitionPlatform.dto.FilterQuestionRequest;
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

    @PostMapping
    public ResponseEntity<Object> createQuestion(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                 @RequestBody CreateQuestionRequest createQuestionRequest) {
        return questionService.createQuestion(authorizationHeader, createQuestionRequest);
    }

    @PostMapping("/filterQuestion")
    public ResponseEntity<Object> filterQuestion(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                 @RequestBody FilterQuestionRequest filterQuestionRequest){
        return questionService.filterQuestion(authorizationHeader, filterQuestionRequest);
    }
}
