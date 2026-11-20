package com.workmanager.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Fournit les connexions JDBC à PostgreSQL, avec les paramètres lus dans le fichier .env.
public class DatabaseConnection {
    // Charge le fichier .env
    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    // Ouvre une nouvelle connexion (à fermer par l'appelant)
    public static Connection getConnection() throws SQLException {

        // Erreur claire si un paramètre du .env manque
        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException(
                    "Configuration de la base de données incomplète."
            );
        }

        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

}
