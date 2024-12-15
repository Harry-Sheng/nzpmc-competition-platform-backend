package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.JwtService;
import com.nzpmc.CompetitionPlatform.Service.UserService;
import com.nzpmc.CompetitionPlatform.dto.UpdateNameRequest;
import com.nzpmc.CompetitionPlatform.dto.UserSignUpRequest;
import com.nzpmc.CompetitionPlatform.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Object> registerUser(@RequestBody UserSignUpRequest UserSignUpRequest) {
        // Check if user already exists
        if (userService.existsById(UserSignUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }
        // Encrypt the password before saving
        String passwordHash = passwordEncoder.encode(UserSignUpRequest.getPassword());

        User user = new User(
                UserSignUpRequest.getName(),
                UserSignUpRequest.getEmail(),
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
        try{
            String token = jwtService.extractToken(authorizationHeader);
            if (token == null) return ResponseEntity.badRequest().body("Authorization header missing or invalid");

            Claims claims;
            try {
                claims = jwtService.extractAllClaims(token);
            } catch (JwtException e) {
                return ResponseEntity.badRequest().body("Token invalid");
            }

            String email = claims.get("email", String.class);
            String nameToUpdateTo = updateNameRequest.getName();
            User updatedUser = userService.updateName(email, nameToUpdateTo);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalStateException e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
