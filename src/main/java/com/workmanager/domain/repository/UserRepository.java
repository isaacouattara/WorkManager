package com.workmanager.domain.repository;

import com.workmanager.domain.model.User;

import java.util.Optional;
import java.util.UUID;

// Contrat d'accès aux utilisateurs : dit ce qu'on peut faire, pas comment.
public interface UserRepository {

    // Enregistre un utilisateur
    void save(User user);

    // Recherche par UUID
    Optional<User> findById(UUID id);

    // Recherche par e-mail
    Optional<User> findByEmail(String email);
}
