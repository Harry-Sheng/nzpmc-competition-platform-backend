package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.SubmitAttemptRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Attempt;
import com.nzpmc.CompetitionPlatform.repository.AttemptRepository;
import com.nzpmc.CompetitionPlatform.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttemptService {
    private final AttemptRepository attemptRepository;

    private final CompetitionRepository competitionRepository;
    @Autowired
    public AttemptService(AttemptRepository attemptRepository,
                          CompetitionRepository competitionRepository){
        this.competitionRepository = competitionRepository;
        this.attemptRepository = attemptRepository;
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
}
