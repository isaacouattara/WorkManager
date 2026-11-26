package com.workmanager.domain.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        // Message volontairement vague : on ne révèle pas si c'est l'e-mail ou le mot de passe
        super("Identifiants invalides");
    }
}