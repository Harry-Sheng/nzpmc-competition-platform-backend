package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.CreateQuestionRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Question;
import com.nzpmc.CompetitionPlatform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final JwtService jwtService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                          JwtService jwtService){
        this.questionRepository = questionRepository;
        this.jwtService = jwtService;
    }
    public List<Question> getQuestions(String authorizationHeader) {
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
        }
        return this.questionRepository.findAll();
    }

    public ResponseEntity<Object> createQuestion(String authorizationHeader, CreateQuestionRequest request){
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
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

        // Save the question
        questionRepository.save(question);
        return ResponseEntity.ok("Question created successfully: " + question);
    }
}
