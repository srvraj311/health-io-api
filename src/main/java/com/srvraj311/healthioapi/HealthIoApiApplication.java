package com.srvraj311.healthioapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing(modifyOnCreate = true)
public class HealthIoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthIoApiApplication.class, args);
    }

}
