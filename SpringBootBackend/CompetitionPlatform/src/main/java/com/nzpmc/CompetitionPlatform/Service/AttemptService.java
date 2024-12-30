package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.ResultResponse;
import com.nzpmc.CompetitionPlatform.dto.SubmitAttemptRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Attempt;
import com.nzpmc.CompetitionPlatform.models.Question;
import com.nzpmc.CompetitionPlatform.repository.AttemptRepository;
import com.nzpmc.CompetitionPlatform.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttemptService {
    private final AttemptRepository attemptRepository;

    private final CompetitionRepository competitionRepository;

    private final CompetitionService competitionService;

    private final JwtService jwtService;
    @Autowired
    public AttemptService(AttemptRepository attemptRepository,
                          CompetitionRepository competitionRepository,
                          CompetitionService competitionService,
                          JwtService jwtService){
        this.competitionRepository = competitionRepository;
        this.attemptRepository = attemptRepository;
        this.competitionService = competitionService;
        this.jwtService = jwtService;
    }


    public void saveAttempt(String competitionId, SubmitAttemptRequest submitAttemptRequest) {
        // Fetch competition by ID
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        if (competitionOptional.isEmpty()) {
            throw new RuntimeException("Competition not found with ID: " + competitionId);
        }

        // Map request data to Attempt entity
        Attempt attempt = new Attempt();
        attempt.setStudentEmail(submitAttemptRequest.getStudentEmail());
        attempt.setCompetitionId(competitionId);
        attempt.setAttempts(submitAttemptRequest.getAttempts());

        // Save attempt
        attemptRepository.save(attempt);
    }

    public List<ResultResponse> generateResults(String authorizationHeader , String competitionId) {
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
        }
        // Fetch competition by ID
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        if (competitionOptional.isEmpty()) {
            throw new RuntimeException("Competition not found with ID: " + competitionId);
        }

        // Fetch all questions for the competition
        List<Question> questions = competitionService.getQuestionsByCompetitionId(competitionId);

        // Fetch all attempts for the competition
        List<Attempt> attempts = attemptRepository.findByCompetitionId(competitionId);

        // Create correct answer convert question list to map title:correctChoice
        Map<String, Integer> correctAnswers = questions.stream()
                .collect(Collectors.toMap(Question::getTitle, Question::getCorrectChoiceIndex));

        // Calculate results
        List<ResultResponse> results = new ArrayList<>();
        for (Attempt attempt : attempts) {
            int correctCount = 0;
            for (Map.Entry<String, Integer> entry : attempt.getAttempts().entrySet()) {
                String questionId = entry.getKey();
                Integer chosenAnswer = entry.getValue();
                if (correctAnswers.containsKey(questionId) && correctAnswers.get(questionId).equals(chosenAnswer)) {
                    correctCount++;
                }
            }
            results.add(new ResultResponse(attempt.getStudentEmail(), correctCount + " out of " + questions.size()));
        }

        return results;

    }
}
