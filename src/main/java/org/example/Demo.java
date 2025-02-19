package org.example;

import org.example.userapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@OpenAPIDefinition(info = @Info(title = "User Orchestration API", version = "1.0", description = "API for managing users and in-memory DB operations"))
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.userapi.repository")
@EntityScan(basePackages = "org.example.userapi.model")
public class Demo {

//    private final UserService userService;
//
//    public Demo(UserService userService) {
//        this.userService = userService;
//    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Demo.class)
                .web(WebApplicationType.SERVLET) // Force web mode
                .run(args);
    }



//    @Override
//    public void run(String... args) {
//        // Call the load function when the application starts
//        userService.loadUsersFromExternalApi();
//    }
}