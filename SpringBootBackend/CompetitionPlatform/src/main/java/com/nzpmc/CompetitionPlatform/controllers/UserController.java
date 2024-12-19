package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.JwtService;
import com.nzpmc.CompetitionPlatform.Service.UserService;
import com.nzpmc.CompetitionPlatform.dto.UpdateNameRequest;
import com.nzpmc.CompetitionPlatform.dto.UserSignUpRequest;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, JwtService jwtService,
                          PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    //User register
    @PostMapping
    public ResponseEntity<Object> registerUser(@RequestBody UserSignUpRequest userSignUpRequest) {
        // Check if user already exists
        if (userService.existsById(userSignUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }
        // Encrypt the password before saving
        String passwordHash = passwordEncoder.encode(userSignUpRequest.getPassword());

        User user = new User(
                userSignUpRequest.getName(),
                userSignUpRequest.getEmail(),
                passwordHash,
                "user"
        );

        // Save the user
        userService.saveUser(user);

        return ResponseEntity.ok(user);
    }

    // Get users
    @GetMapping
    public ResponseEntity<Object> getUsers(){
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    // Update Name
    @PutMapping("/name")
    public ResponseEntity<?> updateUserName(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                            @RequestBody UpdateNameRequest updateNameRequest) {
        // Extract and validate token
        String token = jwtService.extractToken(authorizationHeader);
        if (token == null) return ResponseEntity.badRequest().body("Authorization header missing or invalid");

        // Extract claims from the token
        Claims claims = jwtService.extractAllClaims(token);

        // Retrieve email and update the name
        String email = claims.get("email", String.class);
        String nameToUpdateTo = updateNameRequest.getName();
        User updatedUser = userService.updateName(email, nameToUpdateTo);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/events")
    public ResponseEntity<?> getUserEvents(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Extract JWT token from Authorization header
        String token = jwtService.extractToken(authorizationHeader);
        if (token == null) return ResponseEntity.badRequest().body("Authorization header missing or invalid");

        // Validate and parse JWT token
        Claims claims = jwtService.extractAllClaims(token);

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

        // Fetch events (assuming events are already loaded via @EntityGraph)
        List<Event> events = user.getEvents();

        return ResponseEntity.ok(events);
    }
}
