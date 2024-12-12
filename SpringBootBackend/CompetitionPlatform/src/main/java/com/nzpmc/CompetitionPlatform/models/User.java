package com.nzpmc.CompetitionPlatform.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Document("user")
public class User {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public List<Event> getEvents() {
        return events;
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
