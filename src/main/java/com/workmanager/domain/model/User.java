package com.workmanager.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;

    public User(String firstName, String lastName, String email, String passwordHash){
        this(
                UUID.randomUUID(),
                firstName,
                lastName,
                email,
                passwordHash,
                LocalDateTime.now()
        );
    }

    public User(UUID id, String firstName, String lastName, String email, String passwordHash, LocalDateTime createdAt){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public UUID getId() {

        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
       return email;
    }

    public String getPasswordHash () {
        return passwordHash;
    }

    public void setEmail(String email) {
        if(email == null || email.isBlank() || !email.contains("@")){
            throw new IllegalArgumentException("Adresse email invalide veuillez entrez une adresse mail au bon format");
        }

        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

