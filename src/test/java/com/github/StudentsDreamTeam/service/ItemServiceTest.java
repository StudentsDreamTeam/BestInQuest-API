package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.model.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    @Test
    void testItemDTOConversion() {
        ItemDTO itemDTO = new ItemDTO(
            1,
            "Test Item",
            "Test Description",
            "RARE",
            1.5f,
            1.2f,
            7200L,
            100L
        );

        Item item = Item.fromDTO(itemDTO);

        assertEquals(itemDTO.id(), item.getId());
        assertEquals(itemDTO.name(), item.getName());
        assertEquals(itemDTO.description(), item.getDescription());
        assertEquals(itemDTO.rarity(), item.getRarity());
        assertEquals(itemDTO.xpMultiplier(), item.getXpMultiplier());
        assertEquals(itemDTO.currencyMultiplier(), item.getCurrencyMultiplier());
        assertEquals(itemDTO.duration(), item.getDuration().getSeconds());
        assertEquals(itemDTO.cost(), item.getCost());
    }

    @Test
    void testItemToDTO() {
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setRarity("RARE");
        item.setXpMultiplier(1.5f);
        item.setCurrencyMultiplier(1.2f);
        item.setDuration(java.time.Duration.ofSeconds(7200L));
        item.setCost(100L);

        ItemDTO dto = ItemDTO.fromORM(item);

        assertEquals(item.getId(), dto.id());
        assertEquals(item.getName(), dto.name());
        assertEquals(item.getDescription(), dto.description());
        assertEquals(item.getRarity(), dto.rarity());
        assertEquals(item.getXpMultiplier(), dto.xpMultiplier());
        assertEquals(item.getCurrencyMultiplier(), dto.currencyMultiplier());
        assertEquals(item.getDuration().getSeconds(), dto.duration());
        assertEquals(item.getCost(), dto.cost());
    }

    @Test
    void testNullValues() {
        ItemDTO itemDTO = new ItemDTO(
            null,
            "Test Item",
            null,
            "COMMON",
            1.0f,
            1.0f,
            null,
            50L
        );

        Item item = Item.fromDTO(itemDTO);

        assertNull(item.getId());
        assertNull(item.getDescription());
        assertNull(item.getDuration());

        assertEquals(itemDTO.name(), item.getName());
        assertEquals(itemDTO.rarity(), item.getRarity());
        assertEquals(itemDTO.xpMultiplier(), item.getXpMultiplier());
        assertEquals(itemDTO.currencyMultiplier(), item.getCurrencyMultiplier());
        assertEquals(itemDTO.cost(), item.getCost());
    }
} 