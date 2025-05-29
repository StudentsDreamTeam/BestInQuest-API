package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.dto.ShopDTO;
import com.github.StudentsDreamTeam.enums.Availability;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.service.ShopService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopControllerTest {

    @Mock
    private ShopService shopService;

    @InjectMocks
    private ShopController shopController;

    private ShopDTO createTestShopDTO() {
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setRarity("RARE");
        item.setXpMultiplier(1.5f);
        item.setCurrencyMultiplier(1.2f);
        item.setDuration(Duration.ofHours(24));
        item.setCost(1000L);

        return new ShopDTO(1, 1, 1500L, Availability.AVAILABLE.getValue());
    }

    @Test
    void getAllItems_Success() {
        // Arrange
        ShopDTO testShopDTO = createTestShopDTO();
        List<ShopDTO> shopItems = List.of(testShopDTO);
        when(shopService.getAllShopItems()).thenReturn(shopItems);

        // Act
        List<ShopDTO> result = shopController.getAllItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testShopDTO.itemId(), result.get(0).itemId());
        assertEquals(testShopDTO.cost(), result.get(0).cost());
        assertEquals(testShopDTO.availability(), result.get(0).availability());
        verify(shopService).getAllShopItems();
    }

    @Test
    void addToShop_Success() {
        // Arrange
        ShopDTO testShopDTO = createTestShopDTO();
        when(shopService.addToShop(any(ShopDTO.class))).thenReturn(testShopDTO);

        // Act
        ShopDTO result = shopController.addToShop(testShopDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testShopDTO.id(), result.id());
        assertEquals(testShopDTO.itemId(), result.itemId());
        assertEquals(testShopDTO.cost(), result.cost());
        assertEquals(testShopDTO.availability(), result.availability());
        verify(shopService).addToShop(any(ShopDTO.class));
    }

    @Test
    void removeFromShop_Success() {
        // Arrange
        Integer itemId = 1;
        doNothing().when(shopService).removeFromShop(itemId);

        // Act
        shopController.removeFromShop(itemId);

        // Assert
        verify(shopService).removeFromShop(itemId);
    }

    @Test
    void removeFromShop_WhenItemNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Integer itemId = 99;
        doThrow(new EntityNotFoundException("Item not found in shop"))
            .when(shopService).removeFromShop(itemId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> shopController.removeFromShop(itemId));
        verify(shopService).removeFromShop(itemId);
    }

    @Test
    void updatePrice_Success() {
        // Arrange
        Integer itemId = 1;
        Long newCost = 2000L;
        ShopDTO testShopDTO = createTestShopDTO();
        ShopDTO updatedShopDTO = new ShopDTO(testShopDTO.id(), testShopDTO.itemId(), newCost, testShopDTO.availability());
        when(shopService.updatePrice(itemId, newCost)).thenReturn(updatedShopDTO);

        // Act
        ShopDTO result = shopController.updatePrice(itemId, newCost);

        // Assert
        assertNotNull(result);
        assertEquals(itemId, result.itemId());
        assertEquals(newCost, result.cost());
        assertEquals(testShopDTO.availability(), result.availability());
        verify(shopService).updatePrice(itemId, newCost);
    }

    @Test
    void updatePrice_WhenItemNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Integer itemId = 99;
        Long newCost = 2000L;
        when(shopService.updatePrice(itemId, newCost))
            .thenThrow(new EntityNotFoundException("Item not found in shop"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> shopController.updatePrice(itemId, newCost));
        verify(shopService).updatePrice(itemId, newCost);
    }
} 