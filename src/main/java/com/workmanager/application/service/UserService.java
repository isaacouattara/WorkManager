package com.workmanager.application.service;

import com.workmanager.domain.model.User;
import com.workmanager.domain.repository.UserRepository;
import com.workmanager.infrastructure.security.PasswordHasher;

import java.util.Optional;
import java .util.UUID;

// Service applicatif : cas d'utilisation liés aux utilisateurs.
public class UserService {

    // Dépend de l'interface, pas d'une implémentation (mémoire ou PostgreSQL)
    private final UserRepository userRepository;

    // Hashage du mot de passe
    private final PasswordHasher passwordHasher;

    // Injection de dépendance par le constructeur
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordHasher = new PasswordHasher();
    }

    // Crée un User et l'enregistre (à terme : hacher le mot de passe ici avec PasswordHasher)
    public User createUser(String firstName, String lastName, String email, String password){

        String passwordHash = passwordHasher.hashPassword(password);
        User user = new User(firstName, lastName, email, passwordHash);

        userRepository.save(user);

        return user;

    }

    // Recherche par identifiant (Optional vide si introuvable)
    public Optional<User> findUserById(UUID id){
        return userRepository.findById(id);
    }

    // Recherche par email (Optional vide si introuvable)
    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmailIgnoreCase(email);
    }

}
