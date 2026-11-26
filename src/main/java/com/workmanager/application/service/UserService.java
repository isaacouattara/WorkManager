package com.workmanager.application.service;

import com.workmanager.domain.model.User;
import com.workmanager.domain.repository.UserRepository;
import com.workmanager.infrastructure.security.PasswordHasher;
import com.workmanager.domain.exception.EmailAlreadyUsedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    // Spring injecte les deux beans (un seul constructeur : pas besoin d'@Autowired)
    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    // Crée un utilisateur ; refuse un e-mail déjà utilisé (sans tenir compte de la casse)
    @Transactional
    public User createUser(String firstName, String lastName, String email, String password) {
        String cleanEmail = email.trim();

        if (userRepository.findByEmailIgnoreCase(cleanEmail).isPresent()) {
            throw new EmailAlreadyUsedException();
        }

        User user = new User(firstName.trim(), lastName.trim(), cleanEmail, passwordHasher.hashPassword(password));
        return userRepository.save(user);
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }
}