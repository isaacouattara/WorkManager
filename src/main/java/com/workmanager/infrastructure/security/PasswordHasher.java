package com.workmanager.infrastructure.security;

import org.mindrot.jbcrypt.BCrypt;

// Hachage et vérification des mots de passe avec BCrypt (un hash n'est pas réversible).
public class PasswordHasher {

     // Transforme un mot de passe en hash BCrypt à stocker en base
     public String hashPassword(String password){

         if (password == null || password.isBlank()){
             throw new IllegalArgumentException("Le mot de passe ne peut être vide");
         }

         // gensalt(12) : sel aléatoire + coût 12 (plus c'est élevé, plus c'est lent à attaquer)
         return BCrypt.hashpw(password, BCrypt.gensalt(12));
     }

     // Compare un mot de passe saisi au hash enregistré ; false si incorrect ou null
     public boolean verify(String password, String hashedPassword){

         if (password == null || hashedPassword == null){
             return false;
         }

         return BCrypt.checkpw(password, hashedPassword);
     }

}
