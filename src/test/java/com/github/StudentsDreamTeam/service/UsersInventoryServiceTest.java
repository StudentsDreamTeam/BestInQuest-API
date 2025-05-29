package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import com.github.StudentsDreamTeam.model.Income;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UsersInventory;
import com.github.StudentsDreamTeam.repository.IncomeRepository;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.repository.UsersInventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersInventoryServiceTest {

    private UsersInventoryService usersInventoryService;
    private UserService userService;
    private UsersInventoryRepository inventoryRepo;
    private AchievementDetector achievementDetector;
    private UserRepository userRepo;
    private ItemRepository itemRepo;
    private IncomeRepository incomeRepo;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        inventoryRepo = mock(UsersInventoryRepository.class);
        achievementDetector = mock(AchievementDetector.class);
        userRepo = mock(UserRepository.class);
        itemRepo = mock(ItemRepository.class);
        incomeRepo = mock(IncomeRepository.class);
        
        usersInventoryService = new UsersInventoryService();
        
        // Используем рефлексию для установки mock-объектов
        try {
            var userServiceField = UsersInventoryService.class.getDeclaredField("userService");
            var inventoryRepoField = UsersInventoryService.class.getDeclaredField("inventoryRepo");
            var achievementDetectorField = UsersInventoryService.class.getDeclaredField("achievementDetector");
            var userRepoField = UsersInventoryService.class.getDeclaredField("userRepo");
            var itemRepoField = UsersInventoryService.class.getDeclaredField("itemRepo");
            var incomeRepoField = UsersInventoryService.class.getDeclaredField("incomeRepo");
            
            userServiceField.setAccessible(true);
            inventoryRepoField.setAccessible(true);
            achievementDetectorField.setAccessible(true);
            userRepoField.setAccessible(true);
            itemRepoField.setAccessible(true);
            incomeRepoField.setAccessible(true);
            
            userServiceField.set(usersInventoryService, userService);
            inventoryRepoField.set(usersInventoryService, inventoryRepo);
            achievementDetectorField.set(usersInventoryService, achievementDetector);
            userRepoField.set(usersInventoryService, userRepo);
            itemRepoField.set(usersInventoryService, itemRepo);
            incomeRepoField.set(usersInventoryService, incomeRepo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAll() {
        // Подготовка данных
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        Item item1 = new Item();
        item1.setId(1);
        Item item2 = new Item();
        item2.setId(2);

        UsersInventory inventory1 = new UsersInventory();
        inventory1.setId(1);
        inventory1.setUser(user1);
        inventory1.setItem(item1);
        inventory1.setAmount(5L);
        inventory1.setAcquireDate(LocalDateTime.now());

        UsersInventory inventory2 = new UsersInventory();
        inventory2.setId(2);
        inventory2.setUser(user2);
        inventory2.setItem(item2);
        inventory2.setAmount(3L);
        inventory2.setAcquireDate(LocalDateTime.now());

        List<UsersInventory> inventories = Arrays.asList(inventory1, inventory2);
        when(inventoryRepo.findAll()).thenReturn(inventories);

        // Выполнение
        List<UsersInventoryDTO> result = usersInventoryService.getAll();

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(inventory1.getId(), result.get(0).id());
        assertEquals(inventory1.getUser().getId(), result.get(0).userId());
        assertEquals(inventory1.getItem().getId(), result.get(0).itemId());
        assertEquals(inventory1.getAmount(), result.get(0).amount());
        assertEquals(inventory1.getAcquireDate(), result.get(0).acquireDate());
        
        assertEquals(inventory2.getId(), result.get(1).id());
        assertEquals(inventory2.getUser().getId(), result.get(1).userId());
        assertEquals(inventory2.getItem().getId(), result.get(1).itemId());
        assertEquals(inventory2.getAmount(), result.get(1).amount());
        assertEquals(inventory2.getAcquireDate(), result.get(1).acquireDate());
        
        verify(inventoryRepo).findAll();
    }

    @Test
    void testGetById() {
        // Подготовка данных
        Integer inventoryId = 1;
        User user = new User();
        user.setId(1);
        Item item = new Item();
        item.setId(1);

        UsersInventory inventory = new UsersInventory();
        inventory.setId(inventoryId);
        inventory.setUser(user);
        inventory.setItem(item);
        inventory.setAmount(5L);
        inventory.setAcquireDate(LocalDateTime.now());

        when(inventoryRepo.findById(inventoryId)).thenReturn(Optional.of(inventory));

        // Выполнение
        UsersInventoryDTO result = usersInventoryService.getById(inventoryId);

        // Проверки
        assertNotNull(result);
        assertEquals(inventory.getId(), result.id());
        assertEquals(inventory.getUser().getId(), result.userId());
        assertEquals(inventory.getItem().getId(), result.itemId());
        assertEquals(inventory.getAmount(), result.amount());
        assertEquals(inventory.getAcquireDate(), result.acquireDate());
        
        verify(inventoryRepo).findById(inventoryId);
    }

    @Test
    void testGetByIdNotFound() {
        // Подготовка данных
        Integer inventoryId = 999;
        when(inventoryRepo.findById(inventoryId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> usersInventoryService.getById(inventoryId));
        verify(inventoryRepo).findById(inventoryId);
    }

    @Test
    void testAddItemToInventory() {
        // Подготовка данных
        Integer userId = 1;
        Integer itemId = 1;
        Long amount = 5L;
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setId(itemId);

        UsersInventoryDTO dto = new UsersInventoryDTO(
            null,
            userId,
            itemId,
            amount,
            now
        );

        UsersInventory savedInventory = new UsersInventory();
        savedInventory.setId(1);
        savedInventory.setUser(user);
        savedInventory.setItem(item);
        savedInventory.setAmount(amount);
        savedInventory.setAcquireDate(now);

        when(userRepo.findById(userId.longValue())).thenReturn(Optional.of(user));
        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));
        when(inventoryRepo.save(any(UsersInventory.class))).thenReturn(savedInventory);

        // Выполнение
        UsersInventoryDTO result = usersInventoryService.addItemToInventory(dto);

        // Проверки
        assertNotNull(result);
        assertEquals(savedInventory.getId(), result.id());
        assertEquals(userId, result.userId());
        assertEquals(itemId, result.itemId());
        assertEquals(amount, result.amount());
        assertEquals(now, result.acquireDate());

        verify(userRepo).findById(userId.longValue());
        verify(itemRepo).findById(itemId);
        verify(inventoryRepo).save(any(UsersInventory.class));
        verify(achievementDetector).detectForUser(user);
    }

    @Test
    void testAddItemToInventoryWithNonexistentUser() {
        // Подготовка данных
        Integer userId = 999;
        Integer itemId = 1;
        UsersInventoryDTO dto = new UsersInventoryDTO(
            null,
            userId,
            itemId,
            5L,
            LocalDateTime.now()
        );

        when(userRepo.findById(userId.longValue())).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> usersInventoryService.addItemToInventory(dto));
        
        verify(userRepo).findById(userId.longValue());
        verify(itemRepo, never()).findById(any());
        verify(inventoryRepo, never()).save(any());
        verify(achievementDetector, never()).detectForUser(any());
    }

    @Test
    void testAddItemToInventoryWithNonexistentItem() {
        // Подготовка данных
        Integer userId = 1;
        Integer itemId = 999;
        User user = new User();
        user.setId(userId);

        UsersInventoryDTO dto = new UsersInventoryDTO(
            null,
            userId,
            itemId,
            5L,
            LocalDateTime.now()
        );

        when(userRepo.findById(userId.longValue())).thenReturn(Optional.of(user));
        when(itemRepo.findById(itemId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> usersInventoryService.addItemToInventory(dto));
        
        verify(userRepo).findById(userId.longValue());
        verify(itemRepo).findById(itemId);
        verify(inventoryRepo, never()).save(any());
        verify(achievementDetector, never()).detectForUser(any());
    }

    @Test
    void testRemoveItemFromInventory() {
        // Подготовка данных
        Integer userId = 1;
        Integer itemId = 1;
        
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setId(itemId);
        
        UsersInventory inventory = new UsersInventory();
        inventory.setId(1);
        inventory.setUser(user);
        inventory.setItem(item);
        inventory.setAmount(5L);
        inventory.setAcquireDate(LocalDateTime.now());

        when(inventoryRepo.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.of(inventory));

        // Выполнение
        usersInventoryService.removeItemFromInventory(userId, itemId);

        // Проверки
        verify(inventoryRepo).findByUserIdAndItemId(userId, itemId);
        verify(inventoryRepo).delete(inventory);
        verify(achievementDetector).revertAchievementsForUser(user);
    }

    @Test
    void testRemoveItemFromInventoryNotFound() {
        // Подготовка данных
        Integer userId = 1;
        Integer itemId = 999;

        when(inventoryRepo.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> usersInventoryService.removeItemFromInventory(userId, itemId));
        
        verify(inventoryRepo).findByUserIdAndItemId(userId, itemId);
        verify(inventoryRepo, never()).delete(any());
        verify(achievementDetector, never()).revertAchievementsForUser(any());
    }

    @Test
    void testSellItemFromInventory() {
        // Подготовка данных
        Integer userId = 1;
        Integer itemId = 1;
        Long cost = 100L;
        
        User user = new User();
        user.setId(userId);
        
        Item item = new Item();
        item.setId(itemId);
        item.setName("Test Item");
        item.setCost(cost);
        
        UsersInventory inventory = new UsersInventory();
        inventory.setId(1);
        inventory.setUser(user);
        inventory.setItem(item);
        inventory.setAmount(5L);
        inventory.setAcquireDate(LocalDateTime.now());

        when(userRepo.findById(userId.longValue())).thenReturn(Optional.of(user));
        when(inventoryRepo.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.of(inventory));
        when(incomeRepo.save(any(Income.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Выполнение
        usersInventoryService.sellItemFromInventory(userId, itemId);

        // Проверки
        verify(userRepo).findById(userId.longValue());
        verify(inventoryRepo).findByUserIdAndItemId(userId, itemId);
        verify(inventoryRepo).delete(inventory);
        verify(achievementDetector).detectForUser(user);
        
        verify(incomeRepo).save(argThat(income -> 
            income.getUser().equals(user) &&
            income.getAmount() == cost.intValue() &&
            income.getDescription().contains(item.getName())
        ));
    }

    @Test
    void testSellItemFromInventoryWithNonexistentUser() {
        // Подготовка данных
        Integer userId = 999;
        Integer itemId = 1;

        when(userRepo.findById(userId.longValue())).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> usersInventoryService.sellItemFromInventory(userId, itemId));
        
        verify(userRepo).findById(userId.longValue());
        verify(inventoryRepo, never()).findByUserIdAndItemId(any(), any());
        verify(inventoryRepo, never()).delete(any());
        verify(achievementDetector, never()).detectForUser(any());
        verify(incomeRepo, never()).save(any());
    }

    @Test
    void testSellItemFromInventoryWithNonexistentInventory() {
        // Подготовка данных
        Integer userId = 1;
        Integer itemId = 999;
        
        User user = new User();
        user.setId(userId);

        when(userRepo.findById(userId.longValue())).thenReturn(Optional.of(user));
        when(inventoryRepo.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> usersInventoryService.sellItemFromInventory(userId, itemId));
        
        verify(userRepo).findById(userId.longValue());
        verify(inventoryRepo).findByUserIdAndItemId(userId, itemId);
        verify(inventoryRepo, never()).delete(any());
        verify(achievementDetector, never()).detectForUser(any());
        verify(incomeRepo, never()).save(any());
    }

    @Test
    void testGetInventoryByUserId() {
        // Подготовка данных
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        Item item1 = new Item();
        item1.setId(1);
        Item item2 = new Item();
        item2.setId(2);

        UsersInventory inventory1 = new UsersInventory();
        inventory1.setId(1);
        inventory1.setUser(user);
        inventory1.setItem(item1);
        inventory1.setAmount(5L);
        inventory1.setAcquireDate(LocalDateTime.now());

        UsersInventory inventory2 = new UsersInventory();
        inventory2.setId(2);
        inventory2.setUser(user);
        inventory2.setItem(item2);
        inventory2.setAmount(3L);
        inventory2.setAcquireDate(LocalDateTime.now());

        List<UsersInventory> inventories = Arrays.asList(inventory1, inventory2);
        when(inventoryRepo.findByUserId(userId)).thenReturn(inventories);

        // Выполнение
        List<UsersInventoryDTO> result = usersInventoryService.getInventoryByUserId(userId);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        
        assertEquals(inventory1.getId(), result.get(0).id());
        assertEquals(inventory1.getUser().getId(), result.get(0).userId());
        assertEquals(inventory1.getItem().getId(), result.get(0).itemId());
        assertEquals(inventory1.getAmount(), result.get(0).amount());
        assertEquals(inventory1.getAcquireDate(), result.get(0).acquireDate());
        
        assertEquals(inventory2.getId(), result.get(1).id());
        assertEquals(inventory2.getUser().getId(), result.get(1).userId());
        assertEquals(inventory2.getItem().getId(), result.get(1).itemId());
        assertEquals(inventory2.getAmount(), result.get(1).amount());
        assertEquals(inventory2.getAcquireDate(), result.get(1).acquireDate());
        
        verify(inventoryRepo).findByUserId(userId);
    }

    @Test
    void testGetInventoryByUserIdEmpty() {
        // Подготовка данных
        Integer userId = 1;
        when(inventoryRepo.findByUserId(userId)).thenReturn(List.of());

        // Выполнение
        List<UsersInventoryDTO> result = usersInventoryService.getInventoryByUserId(userId);

        // Проверки
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(inventoryRepo).findByUserId(userId);
    }
} 