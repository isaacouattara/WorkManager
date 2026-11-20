package com.workmanager.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

// Entité métier : une entreprise (en cours d'écriture).
public class Company {

    private UUID id;
    private String name;
    private String description;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // constructeur pour enregistrer une nouvelle entreprise
    // (génère l'UUID et la date de création)
    public Company(String name, String description, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    //

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }


}
