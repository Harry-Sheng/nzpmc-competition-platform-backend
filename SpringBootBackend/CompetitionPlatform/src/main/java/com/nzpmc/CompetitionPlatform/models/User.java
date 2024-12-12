package com.nzpmc.CompetitionPlatform.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
public class User {

    @Id
    private String email;
    private String name;

    public User(String email, String name){
        this.email = email;
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public void setEmail(){
        this.email = email;
    }

    public void setName(){
        this.name = name;
    }

    @Override
    public String toString(){
        return "Student{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
