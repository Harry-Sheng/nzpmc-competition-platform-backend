package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompetitionService {
    private final CompetitionRepository competitionRepository;

    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository){
        this.competitionRepository = competitionRepository;
    }

    public void saveEvent(Competition competition) {
        competitionRepository.save(competition);
    }
}
