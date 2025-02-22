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

import jakarta.annotation.PostConstruct;

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

    public boolean loadUsersFromExternalApi() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ExternalApiConfig config = new ExternalApiConfig();
            String externalApiUrl = externalApiConfig.getDataUrl();

            UserResponse userResponse = restTemplate.getForObject(externalApiUrl, UserResponse.class);

            if (userResponse != null) {
                List<User> users = userResponse.getUsers();

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
                System.out.println("Data successfully imported from API.!!!");
                return true;
            } else {
                System.out.println("No users returned from the API.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while importing users.");
            return false;
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
        return userRepository.findAll((root, query, criteriaBuilder) -> {
            if (lowerKeyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // Returns all records when keyword is empty
            }

            String likePattern = "%" + lowerKeyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("ssn")), likePattern)
            );
        });

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