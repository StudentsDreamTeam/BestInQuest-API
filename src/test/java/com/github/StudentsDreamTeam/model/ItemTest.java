package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testItemCreation() {
        Item item = new Item();
        assertNotNull(item);
    }

    @Test
    void testItemSettersAndGetters() {
        Item item = new Item();
        List<UsersInventory> usersInventory = new ArrayList<>();
        Shop shop = new Shop();
        Duration duration = Duration.ofHours(2);

        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setRarity("LEGENDARY");
        item.setXpMultiplier(2.0f);
        item.setCurrencyMultiplier(1.5f);
        item.setDuration(duration);
        item.setCost(1000L);
        item.setUsersInventory(usersInventory);
        item.setShop(shop);

        assertEquals(1, item.getId());
        assertEquals("Test Item", item.getName());
        assertEquals("Test Description", item.getDescription());
        assertEquals("LEGENDARY", item.getRarity());
        assertEquals(2.0f, item.getXpMultiplier());
        assertEquals(1.5f, item.getCurrencyMultiplier());
        assertEquals(duration, item.getDuration());
        assertEquals(1000L, item.getCost());
        assertEquals(usersInventory, item.getUsersInventory());
        assertEquals(shop, item.getShop());
    }

    @Test
    void testFromDTO() {
        ItemDTO itemDTO = new ItemDTO(
            1,
            "Test Item",
            "Test Description",
            "RARE",
            1.5f,
            1.2f,
            7200L, // 2 hours in seconds
            500L
        );

        Item item = Item.fromDTO(itemDTO);

        assertNotNull(item);
        assertEquals(itemDTO.id(), item.getId());
        assertEquals(itemDTO.name(), item.getName());
        assertEquals(itemDTO.description(), item.getDescription());
        assertEquals(itemDTO.rarity(), item.getRarity());
        assertEquals(itemDTO.xpMultiplier(), item.getXpMultiplier());
        assertEquals(itemDTO.currencyMultiplier(), item.getCurrencyMultiplier());
        assertEquals(Duration.ofSeconds(itemDTO.duration()), item.getDuration());
        assertEquals(itemDTO.cost(), item.getCost());
    }

    @Test
    void testFromDTOWithNullDuration() {
        ItemDTO itemDTO = new ItemDTO(
            1,
            "Test Item",
            "Test Description",
            "RARE",
            1.5f,
            1.2f,
            null,
            500L
        );

        Item item = Item.fromDTO(itemDTO);

        assertNotNull(item);
        assertEquals(itemDTO.id(), item.getId());
        assertEquals(itemDTO.name(), item.getName());
        assertEquals(itemDTO.description(), item.getDescription());
        assertEquals(itemDTO.rarity(), item.getRarity());
        assertEquals(itemDTO.xpMultiplier(), item.getXpMultiplier());
        assertEquals(itemDTO.currencyMultiplier(), item.getCurrencyMultiplier());
        assertNull(item.getDuration());
        assertEquals(itemDTO.cost(), item.getCost());
    }

    @Test
    void testNullableFields() {
        Item item = new Item();
        
        // Description и Duration - единственные nullable поля
        item.setDescription(null);
        item.setDuration(null);
        
        // Проверяем, что null допустим для этих полей
        assertNull(item.getDescription());
        assertNull(item.getDuration());
    }
} 