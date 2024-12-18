package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.CompetitionService;
import com.nzpmc.CompetitionPlatform.dto.CreateQuestionRequest;
import com.nzpmc.CompetitionPlatform.dto.CreateCompetitionRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;

    @Autowired
    public CompetitionController(CompetitionService competitionService){
        this.competitionService = competitionService;
    }
    @PostMapping
    public ResponseEntity<Object> addCompetition(@RequestBody CreateCompetitionRequest createCompetitionRequest){
        ArrayList<String> questionIds = new ArrayList<String>();
        Competition competition = new Competition(
                createCompetitionRequest.getTitle(),
                questionIds
        );
        competitionService.saveEvent(competition);
        return ResponseEntity.ok(competition);
    }

    @PostMapping("/{competitionId}/questions")
    public ResponseEntity<Object> addQuestionToCompetition(@PathVariable String competitionId,
                                                           @RequestBody CreateQuestionRequest createQuestionRequest) {
        System.out.println(createQuestionRequest);
        return competitionService.addQuestionToCompetition(competitionId, createQuestionRequest);
    }
}
