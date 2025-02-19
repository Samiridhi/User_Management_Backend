package org.example.userapi.service;



import org.example.userapi.config.ExternalApiConfig;
import org.example.userapi.controller.UserResponse;
import org.example.userapi.model.Address;
import org.example.userapi.model.Company;
import org.example.userapi.model.User;
import org.example.userapi.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;

@Service("userService")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ExternalApiConfig externalApiConfig;


    private final UserRepository userRepository;

    public UserService (ExternalApiConfig externalApiConfig, UserRepository userRepository) {
        this.externalApiConfig = externalApiConfig;
        this.userRepository = userRepository;
    }
    @PostConstruct
    public void init() {
        loadUsersFromExternalApi();
    }

    public void loadUsersFromExternalApi() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String externalApiUrl = externalApiConfig.getDataUrl();
            System.out.println("externalApiUrl:"+externalApiUrl);
            // Fetch and directly convert JSON response to UserResponse class
            UserResponse userResponse = restTemplate.getForObject("https://dummyjson.com/users", UserResponse.class);

            if (userResponse != null) {
                List<User> users = userResponse.getUsers();

                // Ensure proper mapping for nested objects
                for (User user : users) {
                    if (user.getCompany() == null) {
                        user.setCompany(new Company());
                    }
                    if (user.getAddress() == null) {
                        user.setAddress(new Address());
                    }
                    if (user.getId() == null) {
                        throw new IllegalArgumentException("User ID is mandatory and missing for: " + user.getFirstName());
                    }
                }

                userRepository.saveAll(users);
                System.out.println("Data successfully imported from API.");
            } else {
                System.out.println("No users returned from the API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while importing users.");
        }
    }


//    public void loadUsersFromExternalApi() {
//        try {
//            logger.info("Fetching users from external API: {}", externalApiUrl);
//            ResponseEntity<UserResponse> response = restTemplate.getForEntity(externalApiUrl, UserResponse.class);
//            if (response.getBody() != null) {
//                List<User> users = response.getBody().getUsers();
//                userRepository.saveAll(users);
//                logger.info("Successfully loaded {} users into the database", users.size());
//            }
//        } catch (Exception e) {
//            logger.error("Error fetching users from external API", e);
//        }
//    }



    public List<User> searchUsers(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        logger.info("Searching users with keyword: {}", lowerKeyword);
        return userRepository.findAll((root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + lowerKeyword + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lowerKeyword + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("ssn")), "%" + lowerKeyword + "%")
                )
        );
    }

    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }
}