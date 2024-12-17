package com.nzpmc.CompetitionPlatform.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document("user")
public class User {

    @Setter
    @Id
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Password hash is required")
    private String passwordHash;

    @NotNull
    private String role = "user";

    @DBRef // Establishes a reference to the Event documents
    private List<Event> events = new ArrayList<>();

    public User() {
        this.events = new ArrayList<>();
    }

    public User(String name, String email, String passwordHash, String role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.events = new ArrayList<Event>();
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    @Override
    public String toString(){
        return "User{" +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
