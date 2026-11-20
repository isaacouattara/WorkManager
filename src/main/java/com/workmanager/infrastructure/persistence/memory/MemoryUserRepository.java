package com.workmanager.infrastructure.persistence.memory;

import com.workmanager.domain.model.User;
import com.workmanager.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// Repository qui stocke les utilisateurs en mémoire (perdus à l'arrêt du programme).
public class MemoryUserRepository implements UserRepository{

    // Clé = id de l'utilisateur
    private  final Map<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user){
        users.put(user.getId(), user);
    }

    // ofNullable : Optional vide si l'id est inconnu
    @Override
    public Optional<User> findById(UUID id){
        return Optional.ofNullable(users.get(id));
    }

    // Parcourt les utilisateurs ; equalsIgnoreCase ignore la casse de l'e-mail
    @Override
    public Optional<User> findByEmail(String email){

        for (User user : users.values()) {
            if(user.getEmail().equalsIgnoreCase(email)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }
}
