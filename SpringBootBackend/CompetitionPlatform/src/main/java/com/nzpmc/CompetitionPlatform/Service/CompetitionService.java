package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.CreateQuestionRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Question;
import com.nzpmc.CompetitionPlatform.repository.CompetitionRepository;
import com.nzpmc.CompetitionPlatform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompetitionService {
    private final CompetitionRepository competitionRepository;

    private final QuestionRepository questionRepository;

    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository,
                              QuestionRepository questionRepository){
        this.competitionRepository = competitionRepository;
        this.questionRepository = questionRepository;
    }

    public void saveEvent(Competition competition) {
        competitionRepository.save(competition);
    }

    public ResponseEntity<Object>  addQuestionToCompetition(String competitionId, CreateQuestionRequest request){
        // Fetch competition by ID
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        if (competitionOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Competition not found with ID: " + competitionId);
        }

        // Create a new Question
        Question question = new Question();
        question.setTitle(request.getTitle());
        question.setOptions(request.getOptions());
        question.setCorrectChoiceIndex(request.getCorrectChoiceIndex());

        // Save the question
        questionRepository.save(question);

        //Check if competition already have this question
        Competition competition = competitionOptional.get();
        String questionTitle = request.getTitle();
        if (competition.getQuestionIds().stream().anyMatch(e -> e.equals(questionTitle))) {
            return ResponseEntity.badRequest().body("This question already exists in the competition.");
        }

        // Associate the question with the competition
        competition.getQuestionIds().add(question.getTitle());
        competitionRepository.save(competition);
        return ResponseEntity.ok("Question added successfully to competition ID: " + competitionId);
    }
}
