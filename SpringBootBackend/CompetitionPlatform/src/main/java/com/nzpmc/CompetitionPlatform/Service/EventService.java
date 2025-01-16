package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.CreateEventRequest;
import com.nzpmc.CompetitionPlatform.models.Competition;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.User;
import com.nzpmc.CompetitionPlatform.repository.CompetitionRepository;
import com.nzpmc.CompetitionPlatform.repository.EventRepository;
import com.nzpmc.CompetitionPlatform.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final JwtService jwtService;
    private final CompetitionRepository competitionRepository;

    private final UserService userService;
    @Autowired
    public EventService(EventRepository eventRepository,
                        CompetitionRepository competitionRepository,
                        JwtService jwtService,
                        UserService userService){
        this.eventRepository = eventRepository;
        this.competitionRepository = competitionRepository;
        this.jwtService =  jwtService;
        this.userService = userService;
    }


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event saveEvent(String authorizationHeader, CreateEventRequest createEventRequest) {
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
        }

        Event event = new Event(
                createEventRequest.getName(),
                createEventRequest.getDate(),
                createEventRequest.getDescription(),
                createEventRequest.getCompetitionId()
        );
        return eventRepository.save(event);
    }

    public Optional<Event> findById(String id) {
        return eventRepository.findById(id);
    }

    public Event linkCompetition(String authorizationHeader, String eventId, String competitionId) {
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
        }

        // Fetch Event
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new RuntimeException("Event not found");
        }
        Event event = eventOptional.get();

        // If competitionId is null set to null
        if (competitionId == null){
            event.setCompetitionId(competitionId);
            return eventRepository.save(event);
        }

        // Link Competition to Event
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        if (competitionOptional.isEmpty()) {
            throw new RuntimeException("Competition not found");
        }
        event.setCompetitionId(competitionId);

        // Save updated Event
        return eventRepository.save(event);
    }

    public Map<String, Object> signupForEvent(String authorizationHeader, String eventId) {

        // Validate and parse JWT token
        Claims claims = jwtService.extractAllClaims(authorizationHeader);

        // Extract user ID from token
        String userEmail = claims.get("email", String.class);
        if (userEmail == null) {
            throw new IllegalArgumentException("Token does not contain user email");
        }

        // Retrieve user from the database
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Retrieve event from the database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        // Check if user is already signed up for the event
        boolean isAlreadySignedUp = user.getEvents().stream()
                .anyMatch(e -> e.getName().equals(eventId));
        if (isAlreadySignedUp) {
            throw new IllegalArgumentException("User already signed up for this event");
        }

        // Associate the event with the user
        user.getEvents().add(event);
        userService.save(user);

        // Prepare response data
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User signed up for the event successfully");
        response.put("event", event);
        return response;
    }

    public ResponseEntity<Object> deleteEventById(String authorizationHeader,String eventId) {
        if (!jwtService.isAdmin(authorizationHeader)){
            throw new RuntimeException("Not Admin");
        }

        // Retrieve event from the database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.deleteById(eventId);
        return ResponseEntity.ok(eventId + " is deleted");
    }
}
