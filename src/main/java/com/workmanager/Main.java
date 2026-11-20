
package com.workmanager;

import com.workmanager.application.service.UserService;
import com.workmanager.domain.model.User;
import com.workmanager.infrastructure.persistence.postgres.PostgresUserRepository;

import java.util.Optional;

// Point d'entrée : choisit le repository, l'injecte dans le service et teste les fonctions.
public class Main {

    public static void main(String[] args) {

        // Création du repository
        PostgresUserRepository repository = new PostgresUserRepository();

        // Injection du repertoire dans UserService
        UserService userService = new UserService(repository);

        // Création et enregistrement d'un utilisateur
        // (le 4e argument est une valeur fictive, pas un vrai hash BCrypt)
            User user = userService.createUser(
                    "Isaac",
                    "ouattara",
                    "kris@exemple.com",
                    "empreinte_securisee"
            );

            System.out.println("Utilisateur crée et enregistré !");
            System.out.println("Prénom :"+ user.getFirstName());
            System.out.println("Nom :"+ user.getLastName());
            System.out.println("Email :"+ user.getEmail());


        // Recherche par identifiant (Optional : le résultat peut être absent)
        Optional<User> resultId = userService.findUserById(user.getId());

        if (resultId.isPresent()){
            System.out.println("Utilisateur retrouvé par ID !");
        }

        //Recherche par email
        Optional<User> resultEmail = userService.findUserByEmail(user.getEmail());

        if (resultEmail.isPresent()){
            System.out.println("Utilisateur retrouvé par email !");
        }

    }
}
