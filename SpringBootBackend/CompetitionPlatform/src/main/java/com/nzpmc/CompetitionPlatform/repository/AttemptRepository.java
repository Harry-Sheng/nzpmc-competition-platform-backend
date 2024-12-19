package com.nzpmc.CompetitionPlatform.repository;

import com.nzpmc.CompetitionPlatform.models.Attempt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttemptRepository extends MongoRepository<Attempt, String> {
}
