package com.workmanager.infrastructure.persistence.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.workmanager.domain.model.User;
import com.workmanager.domain.repository.UserRepository;
import com.workmanager.infrastructure.config.DatabaseConnection;

import java.util.Optional;
import java.util.UUID;

// Repository qui stocke les utilisateurs dans la table PostgreSQL "users" avec JDBC.
public class PostgresUserRepository implements UserRepository {


    // INSERT d'un utilisateur ; échoue notamment si l'e-mail existe déjà
    @Override
    public void save (User user){
        // Requête SQL ; les "?" sont remplis plus bas (protège des injections SQL)
        String sql = """
            INSERT INTO users (
                id,
                first_name,
                last_name,
                email,
                password_hash,
                created_at
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        // try-with-resources : connexion et requête fermées automatiquement
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            // Valeurs associées aux "?" (numérotés à partir de 1)
            statement.setObject(1, user.getId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPasswordHash());
            // LocalDateTime -> Timestamp SQL
            statement.setTimestamp(
                    6,
                    java.sql.Timestamp.valueOf(user.getCreatedAt())
            );

            // Exécute l'INSERT
            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur lors de l'enregistrement de l'utilisateur.",
                    e
            );
        }
    }

    // SELECT par identifiant ; Optional vide si introuvable
    @Override
    public Optional<User> findById(UUID id) {

        String sql = """
            SELECT id, first_name, last_name, email, password_hash, created_at
            FROM users
            WHERE id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setObject(1, id);

            // executeQuery : exécute le SELECT et renvoie les lignes trouvées
            try (ResultSet resultSet = statement.executeQuery()) {

                // next() : true s'il existe une ligne
                if (resultSet.next()) {

                    // Reconstruit le User avec le constructeur de restauration
                    User user = new User(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password_hash"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()
                    );

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur lors de la recherche de l'utilisateur.",
                    e
            );
        }

        return Optional.empty();
    }

    // SELECT par e-mail ; LOWER(...) rend la recherche insensible à la casse
    @Override
    public Optional<User> findByEmail(String email){

        String sql = """
                SELECT id, first_name, last_name, email, password_hash, created_at FROM users WHERE LOWER(email) =  LOWER(?)
                """;

        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setString(1,email);

            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()){

                    // Même reconstruction que dans findById
                    User user = new User(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password_hash"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()
                    );

                    return Optional.of(user);
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(
                    "Erreur lors de la recherche d l'utilisateur par email.",
                    e
            );
        }
        return Optional.empty();
    }

}
