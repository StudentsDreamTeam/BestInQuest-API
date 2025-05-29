package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.ShopDTO;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.Shop;
import com.github.StudentsDreamTeam.enums.Availability;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.repository.ShopRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopServiceTest {

    private ShopService shopService;
    private ShopRepository shopRepository;
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        shopRepository = mock(ShopRepository.class);
        itemRepository = mock(ItemRepository.class);
        shopService = new ShopService();
        // Используем рефлексию для установки mock-объектов
        try {
            var shopRepoField = ShopService.class.getDeclaredField("shopRepository");
            var itemRepoField = ShopService.class.getDeclaredField("itemRepository");
            shopRepoField.setAccessible(true);
            itemRepoField.setAccessible(true);
            shopRepoField.set(shopService, shopRepository);
            itemRepoField.set(shopService, itemRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllShopItems() {
        // Подготовка данных
        Item item1 = new Item();
        item1.setId(1);
        Item item2 = new Item();
        item2.setId(2);

        Shop shop1 = new Shop();
        shop1.setId(1);
        shop1.setItem(item1);
        shop1.setCost(100L);
        shop1.setAvailability(Availability.AVAILABLE);

        Shop shop2 = new Shop();
        shop2.setId(2);
        shop2.setItem(item2);
        shop2.setCost(200L);
        shop2.setAvailability(Availability.LIMITED);

        List<Shop> shops = Arrays.asList(shop1, shop2);

        when(shopRepository.findAll()).thenReturn(shops);

        // Вызов тестируемого метода
        List<ShopDTO> result = shopService.getAllShopItems();

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(shop1.getId(), result.get(0).id());
        assertEquals(shop2.getId(), result.get(1).id());
        verify(shopRepository).findAll();
    }

    @Test
    void testAddToShop() {
        // Подготовка данных
        Item item = new Item();
        item.setId(1);
        
        ShopDTO dto = new ShopDTO(null, item.getId(), 100L, Availability.AVAILABLE.getValue());
        
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);
        shop.setCost(100L);
        shop.setAvailability(Availability.AVAILABLE);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);

        // Вызов тестируемого метода
        ShopDTO result = shopService.addToShop(dto);

        // Проверки
        assertNotNull(result);
        assertEquals(shop.getId(), result.id());
        assertEquals(item.getId(), result.itemId());
        assertEquals(shop.getCost(), result.cost());
        assertEquals(shop.getAvailability().getValue(), result.availability());
        
        verify(itemRepository).findById(item.getId());
        verify(shopRepository).save(any(Shop.class));
    }

    @Test
    void testAddToShopWithNonexistentItem() {
        ShopDTO dto = new ShopDTO(null, 999, 100L, Availability.AVAILABLE.getValue());
        
        when(itemRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shopService.addToShop(dto));
        verify(itemRepository).findById(999);
        verify(shopRepository, never()).save(any(Shop.class));
    }

    @Test
    void testRemoveFromShop() {
        Integer itemId = 1;
        Item item = new Item();
        item.setId(itemId);
        
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);

        when(shopRepository.findByItemId(itemId)).thenReturn(Optional.of(shop));

        shopService.removeFromShop(itemId);

        verify(shopRepository).findByItemId(itemId);
        verify(shopRepository).delete(shop);
    }

    @Test
    void testRemoveNonexistentShopItem() {
        Integer itemId = 999;
        when(shopRepository.findByItemId(itemId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shopService.removeFromShop(itemId));
        verify(shopRepository).findByItemId(itemId);
        verify(shopRepository, never()).delete(any(Shop.class));
    }

    @Test
    void testUpdatePrice() {
        Integer itemId = 1;
        Long newCost = 200L;
        Item item = new Item();
        item.setId(itemId);
        
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);
        shop.setCost(100L);
        shop.setAvailability(Availability.AVAILABLE);

        Shop updatedShop = new Shop();
        updatedShop.setId(1);
        updatedShop.setItem(item);
        updatedShop.setCost(newCost);
        updatedShop.setAvailability(Availability.AVAILABLE);

        when(shopRepository.findByItemId(itemId)).thenReturn(Optional.of(shop));
        when(shopRepository.save(any(Shop.class))).thenReturn(updatedShop);

        ShopDTO result = shopService.updatePrice(itemId, newCost);

        assertNotNull(result);
        assertEquals(newCost, result.cost());
        verify(shopRepository).findByItemId(itemId);
        verify(shopRepository).save(shop);
    }

    @Test
    void testUpdatePriceForNonexistentItem() {
        Integer itemId = 999;
        Long newCost = 200L;

        when(shopRepository.findByItemId(itemId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shopService.updatePrice(itemId, newCost));
        verify(shopRepository).findByItemId(itemId);
        verify(shopRepository, never()).save(any(Shop.class));
    }

    @Test
    void testShopDTOConversion() {
        // Create Item
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setRarity("RARE");
        item.setXpMultiplier(1.5f);
        item.setCurrencyMultiplier(1.2f);
        item.setDuration(java.time.Duration.ofSeconds(7200L));
        item.setCost(100L);

        // Create ShopDTO
        ShopDTO shopDTO = new ShopDTO(
            1,
            item.getId(),
            200L,
            Availability.AVAILABLE.getValue()
        );

        // Convert to Shop
        Shop shop = Shop.fromDTO(shopDTO, item);

        // Verify conversion
        assertEquals(shopDTO.id(), shop.getId());
        assertEquals(shopDTO.itemId(), shop.getItem().getId());
        assertEquals(shopDTO.cost(), shop.getCost());
        assertEquals(Availability.fromValue(shopDTO.availability()), shop.getAvailability());
    }

    @Test
    void testShopToDTO() {
        // Create Item
        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setRarity("RARE");
        item.setXpMultiplier(1.5f);
        item.setCurrencyMultiplier(1.2f);
        item.setDuration(java.time.Duration.ofSeconds(7200L));
        item.setCost(100L);

        // Create Shop
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);
        shop.setCost(200L);
        shop.setAvailability(Availability.AVAILABLE);

        // Convert to DTO
        ShopDTO dto = ShopDTO.fromORM(shop);

        // Verify conversion
        assertEquals(shop.getId(), dto.id());
        assertEquals(shop.getItem().getId(), dto.itemId());
        assertEquals(shop.getCost(), dto.cost());
        assertEquals(shop.getAvailability().getValue(), dto.availability());
    }

    @Test
    void testNullValues() {
        // Create Item
        Item item = new Item();
        item.setId(1);

        // Create ShopDTO with null id
        ShopDTO shopDTO = new ShopDTO(
            null,
            item.getId(),
            200L,
            Availability.AVAILABLE.getValue()
        );

        // Convert to Shop
        Shop shop = Shop.fromDTO(shopDTO, item);

        // Verify null values are handled correctly
        assertNull(shop.getId());
        
        // Verify non-null values
        assertEquals(shopDTO.itemId(), shop.getItem().getId());
        assertEquals(shopDTO.cost(), shop.getCost());
        assertEquals(Availability.fromValue(shopDTO.availability()), shop.getAvailability());
    }

    @Test
    void testAvailabilityValues() {
        // Create Item
        Item item = new Item();
        item.setId(1);

        // Test AVAILABLE
        ShopDTO availableDTO = new ShopDTO(1, item.getId(), 200L, Availability.AVAILABLE.getValue());
        Shop availableShop = Shop.fromDTO(availableDTO, item);
        assertEquals(Availability.AVAILABLE, availableShop.getAvailability());

        // Test LIMITED
        ShopDTO limitedDTO = new ShopDTO(1, item.getId(), 200L, Availability.LIMITED.getValue());
        Shop limitedShop = Shop.fromDTO(limitedDTO, item);
        assertEquals(Availability.LIMITED, limitedShop.getAvailability());

        // Test OUT_OF_STOCK
        ShopDTO outOfStockDTO = new ShopDTO(1, item.getId(), 200L, Availability.OUT_OF_STOCK.getValue());
        Shop outOfStockShop = Shop.fromDTO(outOfStockDTO, item);
        assertEquals(Availability.OUT_OF_STOCK, outOfStockShop.getAvailability());
    }

    @Test
    void testInvalidAvailability() {
        // Create Item
        Item item = new Item();
        item.setId(1);

        // Test invalid availability value
        ShopDTO invalidDTO = new ShopDTO(1, item.getId(), 200L, "invalid_status");
        assertThrows(IllegalArgumentException.class, () -> Shop.fromDTO(invalidDTO, item));
    }

    @Test
    void testUpdatePriceWithZeroCost() {
        // Подготовка данных
        Integer itemId = 1;
        Long newCost = 0L;
        Item item = new Item();
        item.setId(itemId);
        
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);
        shop.setCost(100L);
        shop.setAvailability(Availability.AVAILABLE);

        Shop updatedShop = new Shop();
        updatedShop.setId(1);
        updatedShop.setItem(item);
        updatedShop.setCost(newCost);
        updatedShop.setAvailability(Availability.AVAILABLE);

        when(shopRepository.findByItemId(itemId)).thenReturn(Optional.of(shop));
        when(shopRepository.save(any(Shop.class))).thenReturn(updatedShop);

        // Выполнение
        ShopDTO result = shopService.updatePrice(itemId, newCost);

        // Проверки
        assertNotNull(result);
        assertEquals(newCost, result.cost());
        verify(shopRepository).findByItemId(itemId);
        verify(shopRepository).save(shop);
    }

    @Test
    void testUpdatePriceWithNegativeCost() {
        // Подготовка данных
        Integer itemId = 1;
        Long newCost = -100L;
        Item item = new Item();
        item.setId(itemId);
        
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);
        shop.setCost(100L);
        shop.setAvailability(Availability.AVAILABLE);

        when(shopRepository.findByItemId(itemId)).thenReturn(Optional.of(shop));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);

        // Вызов метода и проверка результата
        ShopDTO result = shopService.updatePrice(itemId, newCost);
        
        // Проверяем, что цена обновилась
        assertEquals(newCost, result.cost());
        verify(shopRepository).findByItemId(itemId);
        verify(shopRepository).save(shop);
    }

    @Test
    void testAddToShopWithExistingItem() {
        // Подготовка данных
        Item item = new Item();
        item.setId(1);
        
        Shop existingShop = new Shop();
        existingShop.setId(1);
        existingShop.setItem(item);
        existingShop.setCost(100L);
        existingShop.setAvailability(Availability.AVAILABLE);
        
        ShopDTO dto = new ShopDTO(null, item.getId(), 200L, Availability.AVAILABLE.getValue());

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(shopRepository.save(any(Shop.class))).thenReturn(existingShop);

        // Вызов метода и проверка результата
        ShopDTO result = shopService.addToShop(dto);
        
        assertNotNull(result);
        assertEquals(existingShop.getId(), result.id());
        verify(itemRepository).findById(item.getId());
        verify(shopRepository).save(any(Shop.class));
    }

    @Test
    void testAddToShopWithZeroCost() {
        // Подготовка данных
        Item item = new Item();
        item.setId(1);
        
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);
        shop.setCost(0L);
        shop.setAvailability(Availability.AVAILABLE);
        
        ShopDTO dto = new ShopDTO(null, item.getId(), 0L, Availability.AVAILABLE.getValue());

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);

        // Вызов метода и проверка результата
        ShopDTO result = shopService.addToShop(dto);
        
        assertNotNull(result);
        assertEquals(0L, result.cost());
        verify(itemRepository).findById(item.getId());
        verify(shopRepository).save(any(Shop.class));
    }

    @Test
    void testAddToShopWithNegativeCost() {
        // Подготовка данных
        Item item = new Item();
        item.setId(1);
        
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);
        shop.setCost(-100L);
        shop.setAvailability(Availability.AVAILABLE);
        
        ShopDTO dto = new ShopDTO(null, item.getId(), -100L, Availability.AVAILABLE.getValue());

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);

        // Вызов метода и проверка результата
        ShopDTO result = shopService.addToShop(dto);
        
        assertNotNull(result);
        assertEquals(-100L, result.cost());
        verify(itemRepository).findById(item.getId());
        verify(shopRepository).save(any(Shop.class));
    }

    @Test
    void testAddToShopWithInvalidAvailability() {
        // Подготовка данных
        Item item = new Item();
        item.setId(1);
        
        Shop shop = new Shop();
        shop.setId(1);
        shop.setItem(item);
        shop.setCost(100L);
        shop.setAvailability(Availability.AVAILABLE);
        
        ShopDTO dto = new ShopDTO(null, item.getId(), 100L, "INVALID_STATUS");

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);

        // Проверяем, что создается исключение при неверном статусе
        assertThrows(IllegalArgumentException.class, () -> shopService.addToShop(dto));
        
        verify(itemRepository).findById(item.getId());
        verify(shopRepository, never()).save(any(Shop.class));
    }
} 