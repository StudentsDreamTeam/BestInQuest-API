package com.github.StudentsDreamTeam.inventory;

import com.github.StudentsDreamTeam.repository.IncomeRepository;
import com.github.StudentsDreamTeam.repository.ItemRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.repository.UsersInventoryRepository;
import com.github.StudentsDreamTeam.service.AchievementDetector;
import com.github.StudentsDreamTeam.service.UserService;
import com.github.StudentsDreamTeam.service.UsersInventoryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UsersInventoryServiceTests {
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
}
