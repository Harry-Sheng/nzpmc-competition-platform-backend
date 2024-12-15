package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.EventService;
import com.nzpmc.CompetitionPlatform.Service.JwtService;
import com.nzpmc.CompetitionPlatform.Service.UserService;
import com.nzpmc.CompetitionPlatform.dto.CreateEventRequest;
import com.nzpmc.CompetitionPlatform.dto.UserSignUpRequest;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public EventController(EventService eventService, JwtService jwtService,
                           PasswordEncoder passwordEncoder){
        this.eventService = eventService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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
                createEventRequest.getDescription()
        );
        // Save the user
        eventService.saveEvent(event);
        return ResponseEntity.ok(event);
    }
}
