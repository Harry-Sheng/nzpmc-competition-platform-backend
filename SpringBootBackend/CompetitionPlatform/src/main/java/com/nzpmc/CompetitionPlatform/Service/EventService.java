package com.nzpmc.CompetitionPlatform.Service;

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

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public Optional<Event> findById(String id) {
        return eventRepository.findById(id);
    }

    public Event linkCompetition(String eventId, String competitionId) {
        // Fetch Event
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new RuntimeException("Event not found");
        }
        Event event = eventOptional.get();

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
        // Extract JWT token from Authorization header
        String token = jwtService.extractToken(authorizationHeader);
        if (token == null) {
            throw new IllegalArgumentException("Authorization header missing or invalid");
        }

        // Validate and parse JWT token
        Claims claims = jwtService.extractAllClaims(token);

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
}
