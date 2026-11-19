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

public class PostgresUserRepository implements UserRepository {


    @Override
    public void save (User user){
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

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setObject(1, user.getId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPasswordHash());
            statement.setTimestamp(
                    6,
                    java.sql.Timestamp.valueOf(user.getCreatedAt())
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erreur lors de l'enregistrement de l'utilisateur.",
                    e
            );
        }
    }

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

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

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
