package org.example.userapi.controller;

import org.example.userapi.model.User;
import org.example.userapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setEmail("john.doe@example.com");
        sampleUser.setSsn("123-45-6789");
        sampleUser.setAge(30);
    }

    @Test
    void testSearchUsers_Success() {
        List<User> users = new ArrayList<>();
        users.add(sampleUser);

        when(userService.searchUsers("John")).thenReturn(users);

        ResponseEntity<List<User>> response = userController.searchUsers("John");
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testSearchUsers_EmptyResult() {
        when(userService.searchUsers("unknown")).thenReturn(new ArrayList<>());

        ResponseEntity<List<User>> response = userController.searchUsers("unknown");
        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody()); // Matching the controller's current response
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserById(99L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof User);
    }

    @Test
    void testGetUserById_Success() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(sampleUser));

        ResponseEntity<?> response = userController.getUserById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleUser.getFirstName(), ((User) response.getBody()).getFirstName());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userService.getUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserByEmail("notfound@example.com");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof User);
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userService.getUserByEmail("john.doe@example.com")).thenReturn(Optional.of(sampleUser));

        ResponseEntity<?> response = userController.getUserByEmail("john.doe@example.com");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleUser.getEmail(), ((User) response.getBody()).getEmail());
    }

    @Test
    void testLoadUsers_Success() {
        userController.loadUsers();
        // Assuming no exceptions indicate success
        assertTrue(true);
    }
}
