package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.CompetitionService;
import com.nzpmc.CompetitionPlatform.dto.*;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/getById")
    public ResponseEntity<Object> getCompetitionById( @RequestBody GetCompetitionByIdRequest getCompetitionByIdRequest){
        Optional<Competition> competitions = competitionService.getCompetitionById(getCompetitionByIdRequest.getCompetitionId());
        return ResponseEntity.ok(competitions);
    }
    @PostMapping
    public ResponseEntity<Object> addCompetition(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                 @RequestBody CreateCompetitionRequest createCompetitionRequest){
        return  competitionService.createCompetition(authorizationHeader, createCompetitionRequest);
    }

    @PostMapping("/{competitionId}/questions")
    public ResponseEntity<Object> addQuestionToCompetition(@PathVariable String competitionId,
                                                           @RequestHeader(value = "Authorization") String authorizationHeader,
                                                           @RequestBody AddQuestionToCompetitionRequest addQuestionToCompetitionRequest) {
        return competitionService.addQuestionToCompetition(authorizationHeader, competitionId, addQuestionToCompetitionRequest);
    }

    @GetMapping("/{competitionId}/questions")
    public ResponseEntity<Object> getQuestionsByCompetitionId(@PathVariable String competitionId) {
        List<Question> questions = competitionService.getQuestionsByCompetitionId(competitionId);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/inInCompetitionTime")
    public boolean isInCompetitionTime(@RequestBody IsInCompetitionTime isInCompetitionTime) {
        return competitionService.isInCompetitionTime(isInCompetitionTime.getCompetitionId());
    }

    @DeleteMapping("/{competitionId}")
    public ResponseEntity<Object> deleteCompetition(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                    @PathVariable String competitionId) {
        return competitionService.deleteCompetitionById(authorizationHeader, competitionId);
    }
}
