package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.EventService;
import com.nzpmc.CompetitionPlatform.Service.JwtService;
import com.nzpmc.CompetitionPlatform.Service.UserService;
import com.nzpmc.CompetitionPlatform.dto.CreateEventRequest;
import com.nzpmc.CompetitionPlatform.dto.LinkCompetitionRequest;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final JwtService jwtService;
    @Autowired
    public EventController(EventService eventService, JwtService jwtService,
                           UserService userService){
        this.eventService = eventService;
        this.jwtService = jwtService;
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<Object> getEvents(){
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<Object> registerEvent(@RequestBody CreateEventRequest createEventRequest) {
        Event event = new Event(
                createEventRequest.getName(),
                createEventRequest.getDate(),
                createEventRequest.getDescription(),
                createEventRequest.getCompetitionId()
        );
        // Save the user
        eventService.saveEvent(event);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{eventId}/signup")
    public ResponseEntity<Object> signupForEvent(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                 @PathVariable String eventId) {
        // Extract JWT token from Authorization header
        String token = jwtService.extractToken(authorizationHeader);
        if (token == null) return ResponseEntity.badRequest().body("Authorization header missing or invalid");

        // Validate and parse JWT token
        Claims claims;
        try {
            claims = jwtService.extractAllClaims(token);
        } catch (JwtException e) {
            return ResponseEntity.badRequest().body("Token invalid");
        }

        // Extract user ID from token
        String userEmail = claims.get("email", String.class);
        if (userEmail == null) {
            return ResponseEntity.badRequest().body("Token does not contain user email");
        }

        // Retrieve user from the database
        Optional<User> userOptional = userService.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOptional.get();

        // Retrieve event from the database
        Optional<Event> eventOptional = eventService.findById(eventId);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Event not found");
        }
        Event event = eventOptional.get();

        // Check if user is already signed up for the event
        if (user.getEvents().stream().anyMatch(e -> e.getName().equals(eventId))) {
            return ResponseEntity.badRequest().body("User already signed up for this event");
        }

        // Associate the event with the user
        user.getEvents().add(event);
        userService.save(user);

        // Prepare response data
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User signed up for the event successfully");
        response.put("event", event);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{eventId}/competition")
    public ResponseEntity<Object> linkCompetitionToEvent(
            @PathVariable String eventId,
            @RequestBody LinkCompetitionRequest linkCompetitionRequest) {
        try {
            Event updatedEvent = eventService.linkCompetition(eventId, linkCompetitionRequest.getTitle());
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
