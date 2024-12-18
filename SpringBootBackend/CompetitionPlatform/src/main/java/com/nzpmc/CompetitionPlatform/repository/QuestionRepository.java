package com.nzpmc.CompetitionPlatform.repository;

import com.nzpmc.CompetitionPlatform.models.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
}
