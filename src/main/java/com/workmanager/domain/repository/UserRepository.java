package com.workmanager.domain.repository;

import com.workmanager.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

// Contrat d'accès aux utilisateurs : dit ce qu'on peut faire, pas comment.
public interface UserRepository extends JpaRepository<User, UUID> {

    // Recherche par e-mail
    Optional<User> findByEmailIgnoreCase(String email);
}
