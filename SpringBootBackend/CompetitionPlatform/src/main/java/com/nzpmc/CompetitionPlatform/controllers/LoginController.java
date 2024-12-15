package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.JwtService;
import com.nzpmc.CompetitionPlatform.Service.UserService;
import com.nzpmc.CompetitionPlatform.models.User;
import com.nzpmc.CompetitionPlatform.dto.LoginRequest;
import com.nzpmc.CompetitionPlatform.dto.LoginResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/login")

public class LoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Autowired
    public LoginController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // user login
    @PostMapping
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userService.findByEmail(email).orElse(null);
        boolean passwordCorrect = user != null && passwordEncoder.matches(password, user.getPasswordHash());

        // user does not exist or password is wrong
        if (!(user != null && passwordCorrect)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "invalid email or password");
            return ResponseEntity.status(401).body(error);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("id", user.getEmail());
        claims.put("role", user.getRole());

        String token = jwtService.createToken(claims);

        LoginResponse response = new LoginResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }
}
