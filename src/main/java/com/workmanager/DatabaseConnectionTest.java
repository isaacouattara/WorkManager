package com.workmanager;

import com.workmanager.infrastructure.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

// Test manuel temporaire : vérifie la connexion Java -> PostgreSQL.
public class DatabaseConnectionTest {

    public static void main (String[] args){

        // try-with-resources : la connexion est fermée automatiquement
        try(Connection connection = DatabaseConnection.getConnection()){

            System.out.println("Connection a sql réussie !");
            // getCatalog() : nom de la base courante
            System.out.println("Base de données : " + connection.getCatalog());

        }catch(SQLException e){
            // Serveur injoignable, mauvais identifiants, base introuvable...

            System.out.println("Echec de la connexion à PostgreSQL.");
            System.out.println("Erreur" + e.getMessage());

        }catch(IllegalStateException e){
            // Variable manquante dans le fichier .env

            System.out.println("Configuration incorrecte.");
            System.out.println("Erreur" + e.getMessage());

        }
    }

}
