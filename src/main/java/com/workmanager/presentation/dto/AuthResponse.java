package com.workmanager.presentation.dto;

public record AuthResponse(String token, String tokenType, long expiresInSeconds) {}