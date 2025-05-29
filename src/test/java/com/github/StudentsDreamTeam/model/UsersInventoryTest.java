package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsersInventoryTest {

    @Test
    void testUsersInventoryCreation() {
        UsersInventory inventory = new UsersInventory();
        assertNotNull(inventory);
    }

    @Test
    void testUsersInventorySettersAndGetters() {
        UsersInventory inventory = new UsersInventory();
        User user = new User();
        Item item = new Item();
        LocalDateTime now = LocalDateTime.now();

        inventory.setId(1);
        inventory.setUser(user);
        inventory.setItem(item);
        inventory.setAmount(5L);
        inventory.setAcquireDate(now);

        assertEquals(1, inventory.getId());
        assertEquals(user, inventory.getUser());
        assertEquals(item, inventory.getItem());
        assertEquals(5L, inventory.getAmount());
        assertEquals(now, inventory.getAcquireDate());
    }

    @Test
    void testFromDTO() {
        User user = new User();
        user.setId(1);
        
        Item item = new Item();
        item.setId(2);
        
        LocalDateTime now = LocalDateTime.now();
        
        UsersInventoryDTO dto = new UsersInventoryDTO(
            1,
            1,
            2,
            5L,
            now
        );

        UsersInventory inventory = UsersInventory.fromDTO(dto, user, item);

        assertNotNull(inventory);
        assertEquals(dto.id(), inventory.getId());
        assertEquals(user, inventory.getUser());
        assertEquals(item, inventory.getItem());
        assertEquals(dto.amount(), inventory.getAmount());
        assertEquals(dto.acquireDate(), inventory.getAcquireDate());
    }

    @Test
    void testAmountValidation() {
        UsersInventory inventory = new UsersInventory();
        
        // Проверяем установку положительного количества
        inventory.setAmount(1L);
        assertEquals(1L, inventory.getAmount());
        
        // Проверяем установку нуля
        inventory.setAmount(0L);
        assertEquals(0L, inventory.getAmount());
        
        // Проверяем установку большого числа
        inventory.setAmount(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, inventory.getAmount());
    }
} 