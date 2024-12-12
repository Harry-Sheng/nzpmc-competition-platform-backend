package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.models.User;
import com.nzpmc.CompetitionPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user")

public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User userData){
        userRepository.save(userData);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<Object> getUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
