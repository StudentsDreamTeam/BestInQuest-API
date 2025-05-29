package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UsersInventory;
import com.github.StudentsDreamTeam.service.TaskService;
import com.github.StudentsDreamTeam.service.UsersInventoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersInventoryControllerTest {

    @Mock
    private UsersInventoryService service;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private UsersInventoryController controller;

    private UsersInventoryDTO createTestDTO() {
        return new UsersInventoryDTO(
            1,          // id
            1,          // userId
            1,          // itemId
            1L,         // amount
            LocalDateTime.now() // acquireDate
        );
    }

    @Test
    void addItem_Success() {
        // Arrange
        UsersInventoryDTO testDTO = createTestDTO();
        when(service.addItemToInventory(any(UsersInventoryDTO.class))).thenReturn(testDTO);

        // Act
        UsersInventoryDTO result = controller.addItem(testDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testDTO.id(), result.id());
        assertEquals(testDTO.userId(), result.userId());
        assertEquals(testDTO.itemId(), result.itemId());
        assertEquals(testDTO.amount(), result.amount());
        assertEquals(testDTO.acquireDate(), result.acquireDate());
        verify(service).addItemToInventory(any(UsersInventoryDTO.class));
    }

    @Test
    void addItem_WhenUserOrItemNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        UsersInventoryDTO testDTO = createTestDTO();
        when(service.addItemToInventory(any(UsersInventoryDTO.class)))
            .thenThrow(new EntityNotFoundException("User or Item not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> controller.addItem(testDTO));
        verify(service).addItemToInventory(any(UsersInventoryDTO.class));
    }

    @Test
    void removeItem_Success() {
        // Arrange
        Integer userId = 1;
        Integer itemId = 1;
        doNothing().when(service).removeItemFromInventory(userId, itemId);

        // Act
        controller.removeItem(userId, itemId);

        // Assert
        verify(service).removeItemFromInventory(userId, itemId);
    }

    @Test
    void removeItem_WhenNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Integer userId = 99;
        Integer itemId = 99;
        doThrow(new EntityNotFoundException("Item not found in inventory"))
            .when(service).removeItemFromInventory(userId, itemId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> controller.removeItem(userId, itemId));
        verify(service).removeItemFromInventory(userId, itemId);
    }

    @Test
    void getAll_Success() {
        // Arrange
        List<UsersInventoryDTO> expectedList = List.of(createTestDTO());
        when(service.getAll()).thenReturn(expectedList);

        // Act
        List<UsersInventoryDTO> result = controller.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedList.get(0).id(), result.get(0).id());
        assertEquals(expectedList.get(0).userId(), result.get(0).userId());
        assertEquals(expectedList.get(0).itemId(), result.get(0).itemId());
        verify(service).getAll();
    }

    @Test
    void getById_Success() {
        // Arrange
        UsersInventoryDTO testDTO = createTestDTO();
        when(service.getById(1)).thenReturn(testDTO);

        // Act
        UsersInventoryDTO result = controller.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(testDTO.id(), result.id());
        assertEquals(testDTO.userId(), result.userId());
        assertEquals(testDTO.itemId(), result.itemId());
        verify(service).getById(1);
    }

    @Test
    void getById_WhenNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(service.getById(99))
            .thenThrow(new EntityNotFoundException("Inventory record not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> controller.getById(99));
        verify(service).getById(99);
    }

    @Test
    void getInventoryByUserId_Success() {
        // Arrange
        List<UsersInventoryDTO> expectedList = List.of(createTestDTO());
        Integer userId = 1;
        when(service.getInventoryByUserId(userId)).thenReturn(expectedList);

        // Act
        List<UsersInventoryDTO> result = controller.getInventoryByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedList.get(0).id(), result.get(0).id());
        assertEquals(expectedList.get(0).userId(), result.get(0).userId());
        assertEquals(expectedList.get(0).itemId(), result.get(0).itemId());
        verify(service).getInventoryByUserId(userId);
    }

    @Test
    void getInventoryByUserId_WhenUserNotFound_ReturnsEmptyList() {
        // Arrange
        Integer userId = 99;
        when(service.getInventoryByUserId(userId)).thenReturn(List.of());

        // Act
        List<UsersInventoryDTO> result = controller.getInventoryByUserId(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(service).getInventoryByUserId(userId);
    }

    @Test
    void sellItem_Success() {
        // Arrange
        Integer userId = 1;
        Integer itemId = 1;
        doNothing().when(service).sellItemFromInventory(userId, itemId);

        // Act
        controller.sellItem(userId, itemId);

        // Assert
        verify(service).sellItemFromInventory(userId, itemId);
    }

    @Test
    void sellItem_WhenNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Integer userId = 99;
        Integer itemId = 99;
        doThrow(new EntityNotFoundException("Item not found in inventory"))
            .when(service).sellItemFromInventory(userId, itemId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> controller.sellItem(userId, itemId));
        verify(service).sellItemFromInventory(userId, itemId);
    }
} 