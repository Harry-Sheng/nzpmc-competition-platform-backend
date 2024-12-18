package com.nzpmc.CompetitionPlatform.repository;

import com.nzpmc.CompetitionPlatform.models.Competition;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompetitionRepository extends MongoRepository<Competition, String>{
}