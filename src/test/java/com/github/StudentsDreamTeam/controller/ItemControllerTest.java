package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private ItemDTO createTestItemDTO() {
        return new ItemDTO(
            1,                  // id
            "Test Item",       // name
            "Test Description", // description
            "RARE",            // rarity
            1.5f,              // xpMultiplier
            1.2f,              // currencyMultiplier
            7200L,             // duration (2 hours in seconds)
            1000L              // cost
        );
    }

    @Test
    void create_Success() {
        // Arrange
        ItemDTO inputDto = createTestItemDTO();
        when(itemService.create(any(ItemDTO.class))).thenReturn(inputDto);

        // Act
        ItemDTO result = itemController.create(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(inputDto.id(), result.id());
        assertEquals(inputDto.name(), result.name());
        assertEquals(inputDto.description(), result.description());
        assertEquals(inputDto.rarity(), result.rarity());
        assertEquals(inputDto.xpMultiplier(), result.xpMultiplier());
        assertEquals(inputDto.currencyMultiplier(), result.currencyMultiplier());
        assertEquals(inputDto.duration(), result.duration());
        assertEquals(inputDto.cost(), result.cost());
        verify(itemService).create(any(ItemDTO.class));
    }

    @Test
    void create_WithExistingName_ThrowsIllegalStateException() {
        // Arrange
        ItemDTO inputDto = createTestItemDTO();
        when(itemService.create(any(ItemDTO.class)))
            .thenThrow(new IllegalStateException("Item with this name already exists"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> itemController.create(inputDto));
        verify(itemService).create(any(ItemDTO.class));
    }

    @Test
    void delete_Success() {
        // Arrange
        Integer itemId = 1;
        doNothing().when(itemService).deleteById(itemId);

        // Act
        itemController.delete(itemId);

        // Assert
        verify(itemService).deleteById(itemId);
    }

    @Test
    void delete_NonExistentItem_ThrowsEntityNotFoundException() {
        // Arrange
        Integer itemId = 999;
        doThrow(new EntityNotFoundException("Item not found"))
            .when(itemService).deleteById(itemId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> itemController.delete(itemId));
        verify(itemService).deleteById(itemId);
    }

    @Test
    void get_Success() {
        // Arrange
        Integer itemId = 1;
        ItemDTO expectedItem = createTestItemDTO();
        when(itemService.getById(itemId)).thenReturn(expectedItem);

        // Act
        ItemDTO result = itemController.get(itemId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedItem.id(), result.id());
        assertEquals(expectedItem.name(), result.name());
        assertEquals(expectedItem.description(), result.description());
        assertEquals(expectedItem.rarity(), result.rarity());
        assertEquals(expectedItem.xpMultiplier(), result.xpMultiplier());
        assertEquals(expectedItem.currencyMultiplier(), result.currencyMultiplier());
        assertEquals(expectedItem.duration(), result.duration());
        assertEquals(expectedItem.cost(), result.cost());
        verify(itemService).getById(itemId);
    }

    @Test
    void get_NonExistentItem_ThrowsEntityNotFoundException() {
        // Arrange
        Integer itemId = 999;
        when(itemService.getById(itemId))
            .thenThrow(new EntityNotFoundException("Item not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> itemController.get(itemId));
        verify(itemService).getById(itemId);
    }

    @Test
    void getAll_Success() {
        // Arrange
        ItemDTO item1 = createTestItemDTO();
        ItemDTO item2 = new ItemDTO(
            2,                  // id
            "Item 2",          // name
            "Description 2",    // description
            "LEGENDARY",        // rarity
            2.0f,              // xpMultiplier
            1.5f,              // currencyMultiplier
            3600L,             // duration (1 hour in seconds)
            2000L              // cost
        );
        List<ItemDTO> expectedItems = Arrays.asList(item1, item2);
        when(itemService.getAll()).thenReturn(expectedItems);

        // Act
        List<ItemDTO> result = itemController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(item1.id(), result.get(0).id());
        assertEquals(item1.name(), result.get(0).name());
        assertEquals(item2.id(), result.get(1).id());
        assertEquals(item2.name(), result.get(1).name());
        verify(itemService).getAll();
    }

    @Test
    void getAll_EmptyList() {
        // Arrange
        when(itemService.getAll()).thenReturn(List.of());

        // Act
        List<ItemDTO> result = itemController.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemService).getAll();
    }

    @Test
    void ping_ReturnsOK() {
        // Act
        String result = itemController.ping();

        // Assert
        assertEquals("OK", result);
    }
} 