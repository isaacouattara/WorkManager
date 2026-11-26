package com.workmanager.application.service;

import com.workmanager.domain.model.User;
import com.workmanager.infrastructure.security.JwtService;
import com.workmanager.infrastructure.security.PasswordHasher;
import com.workmanager.presentation.dto.AuthResponse;
import com.workmanager.domain.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    // Résultat du login côté application ; le contrôleur le convertit en DTO
    public record AuthToken(String token, long expiresInSeconds) {}

    private final UserService userService;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public AuthService(UserService userService, PasswordHasher passwordHasher, JwtService jwtService) {
        this.userService = userService;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }

    public User register(String firstName, String lastName, String email, String password) {
        return userService.createUser(firstName, lastName, email, password);
    }

    public AuthToken login(String email, String password) {
        User user = userService.findUserByEmail(email.trim())
                .filter(u -> passwordHasher.verify(password, u.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);

        return new AuthToken(jwtService.generateToken(user), jwtService.getExpirationSeconds());
    }

    public User getCurrentUser(String userId) {
        return userService.findUserById(UUID.fromString(userId))
                .orElseThrow(InvalidCredentialsException::new);
    }
}