package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@OpenAPIDefinition(info = @Info(title = "User Orchestration API", version = "1.0", description = "API for managing users and in-memory DB operations"))
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.userapi.repository")
@EntityScan(basePackages = "org.example.userapi.model")
public class Demo {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Demo.class)
                .run(args);
    }

}