package com.srvraj311.healthioapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableMongoAuditing(modifyOnCreate = true)
@EnableMethodSecurity()
@CrossOrigin(origins = "*")
public class HealthIoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthIoApiApplication.class, args);
    }

}