package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.models.Question;
import com.nzpmc.CompetitionPlatform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
