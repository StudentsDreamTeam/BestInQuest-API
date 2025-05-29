package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.ShopDTO;
import com.github.StudentsDreamTeam.enums.Availability;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {

    @Test
    void testShopCreation() {
        Shop shop = new Shop();
        assertNotNull(shop);
    }

    @Test
    void testShopSettersAndGetters() {
        Shop shop = new Shop();
        Item item = new Item();

        shop.setId(1);
        shop.setItem(item);
        shop.setCost(100L);
        shop.setAvailability(Availability.AVAILABLE);

        assertEquals(1, shop.getId());
        assertEquals(item, shop.getItem());
        assertEquals(100L, shop.getCost());
        assertEquals(Availability.AVAILABLE, shop.getAvailability());
    }

    @Test
    void testFromDTO() {
        Item item = new Item();
        item.setId(1);

        ShopDTO shopDTO = new ShopDTO(
            1,
            1,
            100L,
            Availability.AVAILABLE.getValue()
        );

        Shop shop = Shop.fromDTO(shopDTO, item);

        assertNotNull(shop);
        assertEquals(shopDTO.id(), shop.getId());
        assertEquals(item, shop.getItem());
        assertEquals(shopDTO.cost(), shop.getCost());
        assertEquals(Availability.AVAILABLE, shop.getAvailability());
    }

    @Test
    void testFromDTOWithNullAvailability() {
        Item item = new Item();
        item.setId(1);

        ShopDTO shopDTO = new ShopDTO(
            1,
            1,
            100L,
            null
        );

        assertThrows(IllegalArgumentException.class, () -> Shop.fromDTO(shopDTO, item));
    }
} 