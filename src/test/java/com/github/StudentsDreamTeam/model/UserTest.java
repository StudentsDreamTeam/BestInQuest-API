package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.enums.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testUserSettersAndGetters() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setPassword("password123");
        user.setXp(100);
        user.setLevel(1);
        user.setRegistrationDate(now);
        user.setLastInDate(now);
        user.setStreak(5);

        assertEquals(1, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(100, user.getXp());
        assertEquals(1, user.getLevel());
        assertEquals(now, user.getRegistrationDate());
        assertEquals(now, user.getLastInDate());
        assertEquals(5, user.getStreak());
    }

    @Test
    void testFromDTO() {
        LocalDateTime now = LocalDateTime.now();
        UserDTO userDTO = new UserDTO(
            1,
            "Test User",
            "test@test.com",
            "password123",
            100,
            1,
            now,
            now,
            5
        );

        User user = User.fromDTO(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.name(), user.getName());
        assertEquals(userDTO.xp(), user.getXp());
        assertEquals(userDTO.level(), user.getLevel());
        assertEquals(userDTO.lastInDate(), user.getLastInDate());
        assertEquals(userDTO.registrationDate(), user.getRegistrationDate());
        assertEquals(userDTO.email(), user.getEmail());
        assertEquals(userDTO.password(), user.getPassword());
        assertEquals(userDTO.streak(), user.getStreak());
    }

    @Test
    void testFromDTOWithNull() {
        assertNull(User.fromDTO(null));
    }

    @Test
    void testGetCompletedTasksCount() {
        User user = new User();
        
        Task task1 = new Task();
        task1.setStatus(Status.DONE);
        
        Task task2 = new Task();
        task2.setStatus(Status.NEW);
        
        Task task3 = new Task();
        task3.setStatus(Status.DONE);

        user.setTasks(Arrays.asList(task1, task2, task3));

        assertEquals(2, user.getCompletedTasksCount());
    }

    @Test
    void testGetCompletedTasksCountWithNullTasks() {
        User user = new User();
        user.setTasks(null);
        assertEquals(0, user.getCompletedTasksCount());
    }

    @Test
    void testGetInventoryItemsCount() {
        User user = new User();
        
        UsersInventory item1 = new UsersInventory();
        item1.setAmount(5L);
        
        UsersInventory item2 = new UsersInventory();
        item2.setAmount(3L);

        user.setInventory(Arrays.asList(item1, item2));

        assertEquals(8, user.getInventoryItemsCount());
    }

    @Test
    void testGetInventoryItemsCountWithNullInventory() {
        User user = new User();
        user.setInventory(null);
        assertEquals(0, user.getInventoryItemsCount());
    }

    @Test
    void testGetInventoryItemsCountWithEmptyInventory() {
        User user = new User();
        user.setInventory(new ArrayList<>());
        assertEquals(0, user.getInventoryItemsCount());
    }
} 