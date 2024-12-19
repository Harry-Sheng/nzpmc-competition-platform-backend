package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.UserService;
import com.nzpmc.CompetitionPlatform.dto.LoginRequest;
import com.nzpmc.CompetitionPlatform.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/login")

public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // user login
    @PostMapping
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
