package com.nzpmc.CompetitionPlatform.repository;

import com.nzpmc.CompetitionPlatform.models.Attempt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttemptRepository extends MongoRepository<Attempt, String> {
    List<Attempt> findByCompetitionId(String competitionId);
}
