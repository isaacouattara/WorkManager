package com.workmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Point d'entrée Spring Boot : lance le contexte, la configuration auto et le serveur web.
@SpringBootApplication
public class WorkManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkManagerApplication.class, args);
    }
}
