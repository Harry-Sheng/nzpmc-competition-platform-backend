package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.AddQuestionToCompetitionRequest;
import com.nzpmc.CompetitionPlatform.dto.CreateCompetitionRequest;
import com.nzpmc.CompetitionPlatform.dto.CreateQuestionRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.Question;
import com.nzpmc.CompetitionPlatform.repository.CompetitionRepository;
import com.nzpmc.CompetitionPlatform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetitionService {
    private final CompetitionRepository competitionRepository;

    private final QuestionRepository questionRepository;

    private final JwtService jwtService;
    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository,
                              QuestionRepository questionRepository,
                              JwtService jwtService){
        this.competitionRepository = competitionRepository;
        this.questionRepository = questionRepository;
        this.jwtService = jwtService;
    }

    public void saveEvent(Competition competition) {
        competitionRepository.save(competition);
    }

    public ResponseEntity<Object>  addQuestionToCompetition(String authorizationHeader, String competitionId,  AddQuestionToCompetitionRequest addQuestionToCompetitionRequest){
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
        }

        // Fetch competition by ID
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        if (competitionOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Competition not found with ID: " + competitionId);
        }


        // Find the question by ID
        String questionTitle = addQuestionToCompetitionRequest.getTitle();
        Optional<Question> questionOptional = questionRepository.findById(questionTitle);

        if (questionOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Question not found.");
        }

        //Check if competition already have this question
        Competition competition = competitionOptional.get();
        if (competition.getQuestionIds().stream().anyMatch(e -> e.equals(questionTitle))) {
            return ResponseEntity.badRequest().body("This question already exists in the competition.");
        }

        // Associate the question with the competition
        competition.getQuestionIds().add(questionTitle);
        competitionRepository.save(competition);
        return ResponseEntity.ok("Question added successfully to competition ID: " + competitionId);
    }

    public List<Question> getQuestionsByCompetitionId(String competitionId) {
        // Fetch competition by ID
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        if (competitionOptional.isEmpty()) {
            throw new RuntimeException("Competition not found with ID: " + competitionId);
        }

        // Fetch questions using the question IDs in the competition
        Competition competition = competitionOptional.get();
        List<String> questionIds = competition.getQuestionIds();
        List<Question> questions = questionRepository.findAllById(questionIds);

        // Create a map of question title to their corresponding Question objects
        Map<String, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getTitle, question -> question));

        // Order the questions based on the order of questionIds
        return questionIds.stream().map(questionMap::get).collect(Collectors.toList());
    }

    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAll();
    }

    public ResponseEntity<Object> createCompetition(String authorizationHeader , CreateCompetitionRequest createCompetitionRequest) {
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
        }

        ArrayList<String> questionIds = new ArrayList<String>();
        Competition competition = new Competition(
                createCompetitionRequest.getTitle(),
                questionIds,
                createCompetitionRequest.getStartTime(),
                createCompetitionRequest.getEndTime()
        );
        competitionRepository.save(competition);
        return ResponseEntity.ok(competition);
    }

    public Optional<Competition> getCompetitionById(String competitionId) {
        // Fetch competition by ID
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        if (competitionOptional.isEmpty()) {
            throw new RuntimeException("Competition not found with ID: " + competitionId);
        }
        return competitionOptional;
    }

    public boolean isInCompetitionTime(String competitionId) {
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);

        if (competitionOptional.isPresent()) {
            Competition competition = competitionOptional.get();
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(competition.getStartTime()) && now.isBefore(competition.getEndTime());
        }
        return false;
    }

    public ResponseEntity<Object> deleteCompetitionById(String authorizationHeader, String competitionId) {
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
        }

        // Retrieve competition from the database
        Competition competition =competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        competitionRepository.deleteById(competitionId);
        return ResponseEntity.ok(competitionId + " is deleted");
    }
}
