package com.workmanager;

import com.workmanager.infrastructure.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main (String[] args){

        try(Connection connection = DatabaseConnection.getConnection()){

            System.out.println("Connection a sql réussie !");
            System.out.println("Base de données : " + connection.getCatalog());

        }catch(SQLException e){

            System.out.println("Echec de la connexion à PostgreSQL.");
            System.out.println("Erreur" + e.getMessage());

        }catch(IllegalStateException e){

            System.out.println("Configuration incorrecte.");
            System.out.println("Erreur" + e.getMessage());

        }
    }

}
