package org.example.userapi.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "User API", description = "Endpoints for user operations")
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
    public ResponseEntity<List<User>> searchUsers(@RequestParam @NotBlank(message = "Keyword cannot be blank") String keyword) {
        try {
            List<User> users = userService.searchUsers(keyword);
            if (users.isEmpty()) {
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.ok(users);
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
    public ResponseEntity<User> getUserById(@PathVariable @Positive(message = "ID must be positive") Long id) {
        try {
            return userService.getUserById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.ok(new User()));
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
    public ResponseEntity<?> getUserByEmail(@RequestParam @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email) {
        try {
            return userService.getUserByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.ok(new User()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Load users from external API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users loaded successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(type = "string")) }),
            @ApiResponse(responseCode = "500", description = "Failed to load users",
                    content = @Content)
    })
    @PostMapping("/load")
    public void loadUsers() {
        try {
            userService.loadUsersFromExternalApi();
            ResponseEntity.ok("Users loaded successfully.");
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load users: " + e.getMessage());
        }
    }
}
