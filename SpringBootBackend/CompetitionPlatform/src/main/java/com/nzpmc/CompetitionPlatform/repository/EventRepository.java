package com.nzpmc.CompetitionPlatform.repository;

import com.nzpmc.CompetitionPlatform.models.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
    // Additional query methods can be defined here if needed
}
