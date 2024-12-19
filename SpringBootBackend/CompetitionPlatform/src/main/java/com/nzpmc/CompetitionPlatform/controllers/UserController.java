package com.nzpmc.CompetitionPlatform.controllers;

import com.nzpmc.CompetitionPlatform.Service.UserService;
import com.nzpmc.CompetitionPlatform.dto.UpdateNameRequest;
import com.nzpmc.CompetitionPlatform.dto.UserSignUpRequest;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    //User register
    @PostMapping
    public ResponseEntity<Object> registerUser(@RequestBody UserSignUpRequest userSignUpRequest) {
        Object user = userService.registerUser(userSignUpRequest);
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
    public ResponseEntity<Object> updateUserName(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                            @RequestBody UpdateNameRequest updateNameRequest) {
        User updatedUser = userService.updateUserName(authorizationHeader, updateNameRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/events")
    public ResponseEntity<Object> getUserEvents(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        List<Event> events = userService.getUserEvents(authorizationHeader);
        return ResponseEntity.ok(events);
    }
}
