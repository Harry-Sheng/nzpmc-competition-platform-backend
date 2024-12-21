package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.CompetitionService;
import com.nzpmc.CompetitionPlatform.dto.CreateQuestionRequest;
import com.nzpmc.CompetitionPlatform.dto.CreateCompetitionRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;

    @Autowired
    public CompetitionController(CompetitionService competitionService){
        this.competitionService = competitionService;
    }

    @GetMapping
    public ResponseEntity<Object> getCompetition(){
        List<Competition> competitions = competitionService.getAllCompetitions();
        return ResponseEntity.ok(competitions);
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

    @GetMapping("/{competitionId}/questions")
    public ResponseEntity<Object> getQuestionsByCompetitionId(@PathVariable String competitionId) {
        List<Question> questions = competitionService.getQuestionsByCompetitionId(competitionId);
        return ResponseEntity.ok(questions);
    }
}
