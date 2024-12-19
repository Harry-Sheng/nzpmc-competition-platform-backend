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
        Map<String, Object> response = eventService.signupForEvent(authorizationHeader, eventId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{eventId}/competition")
    public ResponseEntity<Object> linkCompetitionToEvent(
            @PathVariable String eventId,
            @RequestBody LinkCompetitionRequest linkCompetitionRequest) {
        Event updatedEvent = eventService.linkCompetition(eventId, linkCompetitionRequest.getTitle());
        return ResponseEntity.ok(updatedEvent);
    }
}
