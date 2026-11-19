package com.workmanager.infrastructure.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

     public String hashPassword(String password){

         if (password == null || password.isBlank()){
             throw new IllegalArgumentException("Le mot de passe ne peut être vide");
         }

         return BCrypt.hashpw(password, BCrypt.gensalt(12));
     }

     public boolean verify(String password, String hashedPassword){

         if (password == null || hashedPassword == null){
             return false;
         }

         return BCrypt.checkpw(password, hashedPassword);
     }

}
