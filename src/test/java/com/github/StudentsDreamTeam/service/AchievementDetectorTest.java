package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UserAchievementDTO;
import com.github.StudentsDreamTeam.enums.AchievementType;
import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UserAchievement;
import com.github.StudentsDreamTeam.model.UsersInventory;
import com.github.StudentsDreamTeam.repository.AchievementRepository;
import com.github.StudentsDreamTeam.repository.TaskRepository;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AchievementDetectorTest {

    private AchievementDetector achievementDetector;
    private AchievementRepository achievementRepository;
    private UserAchievementRepository userAchievementRepository;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        achievementRepository = mock(AchievementRepository.class);
        userAchievementRepository = mock(UserAchievementRepository.class);
        taskRepository = mock(TaskRepository.class);
        
        achievementDetector = new AchievementDetector(
            achievementRepository,
            userAchievementRepository,
            taskRepository
        );
    }

    @Test
    void testCheckAchievementRequirementsXP() {
        // Подготовка данных
        User user = new User();
        user.setXp(1000);

        Achievement achievement = new Achievement();
        achievement.setType(AchievementType.XP);
        achievement.setRequiredValue(500);

        // Выполнение и проверка
        assertTrue(achievementDetector.checkAchievementRequirements(user, achievement));

        // Проверка с недостаточным XP
        achievement.setRequiredValue(1500);
        assertFalse(achievementDetector.checkAchievementRequirements(user, achievement));
    }

    @Test
    void testCheckAchievementRequirementsStreak() {
        // Подготовка данных
        User user = new User();
        user.setStreak(7);

        Achievement achievement = new Achievement();
        achievement.setType(AchievementType.STREAK);
        achievement.setRequiredValue(5);

        // Выполнение и проверка
        assertTrue(achievementDetector.checkAchievementRequirements(user, achievement));

        // Проверка с недостаточным стриком
        achievement.setRequiredValue(10);
        assertFalse(achievementDetector.checkAchievementRequirements(user, achievement));
    }

    @Test
    void testCheckAchievementRequirementsTasksCompleted() {
        // Подготовка данных
        User user = new User();
        user.setId(1);

        Achievement achievement = new Achievement();
        achievement.setType(AchievementType.TASKS_COMPLETED);
        achievement.setRequiredValue(5);

        when(taskRepository.countCompletedByUser(user)).thenReturn(7L);

        // Выполнение и проверка
        assertTrue(achievementDetector.checkAchievementRequirements(user, achievement));

        // Проверка с большим количеством требуемых задач
        achievement.setRequiredValue(10);
        assertFalse(achievementDetector.checkAchievementRequirements(user, achievement));

        verify(taskRepository, times(2)).countCompletedByUser(user);
    }

    @Test
    void testCheckAchievementRequirementsInventoryItems() {
        // Подготовка данных
        User user = new User();
        user.setId(1);

        Achievement achievement = new Achievement();
        achievement.setType(AchievementType.INVENTORY_ITEMS);
        achievement.setRequiredValue(3);

        // Создаем инвентарь с 5 предметами
        user.setInventory(Arrays.asList(
            createInventoryItem(2L),
            createInventoryItem(3L)
        ));

        // Выполнение и проверка
        assertTrue(achievementDetector.checkAchievementRequirements(user, achievement));

        // Проверка с большим количеством требуемых предметов
        achievement.setRequiredValue(10);
        assertFalse(achievementDetector.checkAchievementRequirements(user, achievement));
    }

    @Test
    void testDetectForUser() {
        // Подготовка данных
        User user = new User();
        user.setId(1);
        user.setXp(1000);
        user.setStreak(7);

        Achievement achievement1 = new Achievement();
        achievement1.setId(1);
        achievement1.setType(AchievementType.XP);
        achievement1.setRequiredValue(500);
        achievement1.setName("XP Master");

        Achievement achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setType(AchievementType.STREAK);
        achievement2.setRequiredValue(10);
        achievement2.setName("Streak Master");

        List<Achievement> availableAchievements = Arrays.asList(achievement1, achievement2);
        when(achievementRepository.findAchievementsNotOwnedByUserNative(user.getId())).thenReturn(availableAchievements);
        when(userAchievementRepository.save(any(UserAchievement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Выполнение
        List<UserAchievementDTO> result = achievementDetector.detectForUser(user);

        // Проверки
        assertNotNull(result);
        assertEquals(1, result.size()); // Только одно достижение должно быть получено (XP)
        assertEquals(achievement1.getName(), result.get(0).achievement().name());
        assertNotNull(result.get(0).acquireDate());

        verify(achievementRepository).findAchievementsNotOwnedByUserNative(user.getId());
        verify(userAchievementRepository).save(any(UserAchievement.class));
    }

    @Test
    void testRevertAchievementsForUser() {
        // Подготовка данных
        User user = new User();
        user.setId(1);
        user.setXp(800);
        user.setStreak(5);

        Achievement achievement1 = new Achievement();
        achievement1.setId(1);
        achievement1.setType(AchievementType.XP);
        achievement1.setRequiredValue(1000);

        Achievement achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setType(AchievementType.STREAK);
        achievement2.setRequiredValue(3);

        UserAchievement ua1 = new UserAchievement();
        ua1.setUser(user);
        ua1.setAchievement(achievement1);

        UserAchievement ua2 = new UserAchievement();
        ua2.setUser(user);
        ua2.setAchievement(achievement2);

        List<UserAchievement> userAchievements = Arrays.asList(ua1, ua2);
        when(userAchievementRepository.findByUser(user)).thenReturn(userAchievements);

        // Выполнение
        achievementDetector.revertAchievementsForUser(user);

        // Проверки
        verify(userAchievementRepository).findByUser(user);
        verify(userAchievementRepository).delete(ua1); // XP достижение должно быть отозвано
        verify(userAchievementRepository, never()).delete(ua2); // Streak достижение должно остаться
    }

    private UsersInventory createInventoryItem(Long amount) {
        UsersInventory item = new UsersInventory();
        item.setAmount(amount);
        return item;
    }
} 