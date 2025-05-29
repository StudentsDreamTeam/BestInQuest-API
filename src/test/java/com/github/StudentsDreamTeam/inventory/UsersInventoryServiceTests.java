package com.github.StudentsDreamTeam.inventory;

import static org.mockito.Mockito.*;

import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import com.github.StudentsDreamTeam.enums.ItemRarity;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UsersInventory;
import com.github.StudentsDreamTeam.repository.IncomeRepository;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.repository.UsersInventoryRepository;
import com.github.StudentsDreamTeam.service.AchievementDetector;
import com.github.StudentsDreamTeam.service.UserService;
import com.github.StudentsDreamTeam.service.UsersInventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsersInventoryServiceTests {

    private static final Duration hour = Duration.ofHours(1);

    private static final LocalDateTime now = LocalDateTime.now();

    @Mock
    private UserService userService;

    @Mock
    private UsersInventoryRepository inventoryRepo;

    @Mock
    private AchievementDetector achievementDetector;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ItemRepository itemRepo;

    @Mock
    private IncomeRepository incomeRepo;

    @InjectMocks
    private UsersInventoryService usersInventoryService;

    private static Item getSampleItem(Integer id) {
        Item sample = new Item();

        sample.setId(id);
        sample.setDuration(hour);
        sample.setName("test");
        sample.setDescription("test");
        sample.setCurrencyMultiplier(1.5f);
        sample.setXpMultiplier(1.5f);
        sample.setRarity(ItemRarity.EPIC.getValue());

        return sample;
    }

    public static User createSampleUser(Integer id) {
        User expected = new User();
        expected.setId(id);
        expected.setName("Juan");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("example@example.org");
        expected.setPassword("some strong password");
        expected.setStreak(0);
        return expected;
    }

    private static UsersInventory createSampleInventory(Integer id, User user, Item item) {
        UsersInventory inventory = new UsersInventory();

        inventory.setId(id);
        inventory.setUser(user);
        inventory.setItem(item);
        inventory.setAmount(3L);
        inventory.setAcquireDate(now);

        return inventory;
    }

    @Test
    public void itemAdditionTest() {
        User user = createSampleUser(0);
        Item item = getSampleItem(0);
        UsersInventory inventory = createSampleInventory(0, user, item);

        UsersInventoryDTO inventoryDTO = new UsersInventoryDTO(0, 0, 0, 2L, now);

        when(userRepo.findById(0L)).thenReturn(Optional.of(user));
        when(itemRepo.findById(0)).thenReturn(Optional.of(item));
        when(achievementDetector.detectForUser(user)).thenReturn(List.of());
        when(inventoryRepo.save(any())).thenReturn(inventory);

        usersInventoryService.addItemToInventory(inventoryDTO);

        verify(userRepo).findById(0L);
        verifyNoMoreInteractions(userRepo);

        verify(itemRepo).findById(0);
        verifyNoMoreInteractions(itemRepo);

        verify(achievementDetector).detectForUser(user);
        verifyNoMoreInteractions(achievementDetector);

        verify(inventoryRepo).save(any());
        verifyNoMoreInteractions(inventoryRepo);

    }

}
