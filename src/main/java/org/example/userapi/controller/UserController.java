package org.example.userapi.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.userapi.model.User;
import org.example.userapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "User API", description = "Endpoints for user operations")
@Validated
public class UserController {

    private final UserService userService;

    // used a constructor based dependency injection to inject service
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Search users by keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found matching users",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
//        return ResponseEntity.ok(null);
        try {
            if(keyword.isEmpty()){
                return ResponseEntity.ok(null);
            }
            List<User> users = userService.searchUsers(keyword);
            if (users.isEmpty()) {
                return ResponseEntity.ok(null);
            } else {
                return ResponseEntity.ok(users);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
//        @Positive(message = "ID must be positive")
        try {
            if(id < 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
            }
            Optional<User> user = userService.getUserById(id);

            if(user.isEmpty()){
                return ResponseEntity.ok(Optional.of(new User()));
            } else {
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid email format",
                    content = @Content)
    })
    @GetMapping("/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam  String email) {
//        @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format")
        try {
            if(email.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
            }
            System.out.println("email received: "+email);
            Optional<User> user = userService.getUserByEmail(email);

            if(user.isEmpty()){
                return ResponseEntity.ok(new User());
            } else {
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Internal server error");
        }
    }

    @Operation(summary = "Load users from external API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users loaded successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(type = "string")) }),
            @ApiResponse(responseCode = "500", description = "Failed to load users",
                    content = @Content)
    })
    @GetMapping ("/load")
    public ResponseEntity<String> loadUsers() {
        try {
            System.out.println("loading users from external api.....");
            if(userService.loadUsersFromExternalApi()){
                return ResponseEntity.ok("Users loaded successfully...!!!");
            }
            else {
                return ResponseEntity.ok("No users returned from the source.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load users: " + e.getMessage());
        }
    }
}
