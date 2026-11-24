package com.workmanager.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

// Entité métier : un compte utilisateur (table "users").
@Entity
@Table(name = "users")
public class User {

    // initialisation des valeurs

    @Id
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    // constructeur sans argument
    protected User() {}

    // constructeur pour crée un nouvel utilisateur
    // (génère l'UUID et la date de création)
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

    // constructeur pour restaurer un utilisateur existant
    // (id et date viennent de la base ; l'e-mail est validé par setEmail)
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

    // Valide l'e-mail (non vide et contenant "@"), sinon lève IllegalArgumentException
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

