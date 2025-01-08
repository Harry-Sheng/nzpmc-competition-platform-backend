package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.CreateQuestionRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Question;
import com.nzpmc.CompetitionPlatform.repository.CompetitionRepository;
import com.nzpmc.CompetitionPlatform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        //Check if question poll already have this question
        String questionTitle = request.getTitle();
        if (questionRepository.findAll().stream().anyMatch(e -> e.getTitle().equals(questionTitle))) {
            return ResponseEntity.badRequest().body("This question already exists in the question poll.");
        }

        //Check if competition already have this question
        Competition competition = competitionOptional.get();
        if (competition.getQuestionIds().stream().anyMatch(e -> e.equals(questionTitle))) {
            return ResponseEntity.badRequest().body("This question already exists in the competition.");
        }

        // Save the question
        questionRepository.save(question);

        // Associate the question with the competition
        competition.getQuestionIds().add(question.getTitle());
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
}
