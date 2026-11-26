package com.workmanager.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// Hachage et vérification des mots de passe avec BCrypt (implémentation Spring Security).
@Component
public class PasswordHasher {

    // Coût 12 : même niveau que précédemment (le sel aléatoire est géré automatiquement)
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // Transforme un mot de passe en hash BCrypt à stocker en base
    public String hashPassword(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe ne peut être vide");
        }

        return encoder.encode(password);
    }

    // Compare un mot de passe saisi au hash enregistré ; false si l'un des deux est null
    public boolean verify(String password, String hashedPassword) {

        if (password == null || hashedPassword == null) {
            return false;
        }

        return encoder.matches(password, hashedPassword);
    }
}