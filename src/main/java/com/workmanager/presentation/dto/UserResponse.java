package com.workmanager.presentation.dto;

import com.workmanager.domain.model.User;

import java.util.UUID;

// Ce que le client a le droit de voir (jamais passwordHash)
public record UserResponse(UUID id, String firstName, String lastName, String email) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}