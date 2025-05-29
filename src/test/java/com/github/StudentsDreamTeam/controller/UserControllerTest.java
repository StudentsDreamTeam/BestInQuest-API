package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.UpdateUserProfileDTO;
import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setXp(100);
        testUser.setLevel(1);
        testUser.setRegistrationDate(LocalDateTime.now());
        testUser.setLastInDate(LocalDateTime.now());
        testUser.setStreak(5);

        testUserDTO = UserDTO.fromORM(testUser);
    }

    @Test
    void getUserProfile_WhenUserExists_ReturnsUserDTO() {
        // Arrange
        when(userService.getUserProfile(1L)).thenReturn(testUser);

        // Act
        UserDTO result = userController.getUserProfile(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getName(), result.name());
        assertEquals(testUser.getEmail(), result.email());
        assertEquals(testUser.getXp(), result.xp());
        assertEquals(testUser.getLevel(), result.level());
        assertEquals(testUser.getStreak(), result.streak());
        verify(userService).getUserProfile(1L);
    }

    @Test
    void getUserProfile_WhenUserDoesNotExist_ThrowsEntityNotFoundException() {
        // Arrange
        when(userService.getUserProfile(99L)).thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userController.getUserProfile(99L));
        verify(userService).getUserProfile(99L);
    }

    @Test
    void getAll_ReturnsListOfUsers() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userService.getAll()).thenReturn(users);

        // Act
        List<UserDTO> result = userController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getName(), result.get(0).name());
        assertEquals(testUser.getEmail(), result.get(0).email());
        verify(userService).getAll();
    }

    @Test
    void ping_ReturnsOK() {
        // Act
        String result = userController.ping();

        // Assert
        assertEquals("OK", result);
    }

    @Test
    void updateProfile_WhenUserExists_ReturnsUpdatedUserDTO() {
        // Arrange
        Integer userId = 1;
        UpdateUserProfileDTO updateDTO = new UpdateUserProfileDTO("Updated Name", "updated@example.com", "newpassword");
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName(updateDTO.name());
        updatedUser.setEmail(updateDTO.email());
        updatedUser.setPassword(updateDTO.password());

        when(userService.updateUserProfile(userId, updateDTO)).thenReturn(updatedUser);

        // Act
        UserDTO result = userController.updateProfile(userId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updateDTO.name(), result.name());
        assertEquals(updateDTO.email(), result.email());
        verify(userService).updateUserProfile(userId, updateDTO);
    }

    @Test
    void updateProfile_WhenUserDoesNotExist_ThrowsEntityNotFoundException() {
        // Arrange
        Integer userId = 99;
        UpdateUserProfileDTO updateDTO = new UpdateUserProfileDTO("Updated Name", "updated@example.com", "newpassword");
        when(userService.updateUserProfile(userId, updateDTO))
            .thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userController.updateProfile(userId, updateDTO));
        verify(userService).updateUserProfile(userId, updateDTO);
    }

    @Test
    void testAdd_CreatesNewUser() {
        // Arrange
        when(userService.registerUser(any(User.class))).thenReturn(testUser);

        // Act
        UserDTO result = userController.testAdd(testUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getName(), result.name());
        assertEquals(testUser.getEmail(), result.email());
        verify(userService).registerUser(any(User.class));
    }
} 