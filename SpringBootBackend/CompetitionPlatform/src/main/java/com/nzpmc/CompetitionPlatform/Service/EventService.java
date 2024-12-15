package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.User;
import com.nzpmc.CompetitionPlatform.repository.EventRepository;
import com.nzpmc.CompetitionPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public Optional<Event> findById(String id) {
        return eventRepository.findById(id);
    }
}
