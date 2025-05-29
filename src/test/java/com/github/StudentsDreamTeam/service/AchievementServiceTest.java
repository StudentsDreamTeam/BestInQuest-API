package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.AchievementDTO;
import com.github.StudentsDreamTeam.enums.AchievementType;
import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UserAchievement;
import com.github.StudentsDreamTeam.repository.AchievementRepository;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AchievementServiceTest {

    private AchievementService achievementService;
    private AchievementRepository achievementRepository;
    private UserAchievementRepository userAchievementRepository;
    private AchievementDetector detector;

    @BeforeEach
    void setUp() {
        achievementRepository = mock(AchievementRepository.class);
        userAchievementRepository = mock(UserAchievementRepository.class);
        detector = mock(AchievementDetector.class);
        achievementService = new AchievementService();
        
        // Используем рефлексию для установки mock-объектов
        try {
            var achievementRepoField = AchievementService.class.getDeclaredField("achievementRepository");
            var userAchievementRepoField = AchievementService.class.getDeclaredField("userAchievementRepository");
            var detectorField = AchievementService.class.getDeclaredField("detector");
            
            achievementRepoField.setAccessible(true);
            userAchievementRepoField.setAccessible(true);
            detectorField.setAccessible(true);
            
            achievementRepoField.set(achievementService, achievementRepository);
            userAchievementRepoField.set(achievementService, userAchievementRepository);
            detectorField.set(achievementService, detector);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAll() {
        // Подготовка данных
        Achievement achievement1 = new Achievement();
        achievement1.setId(1);
        achievement1.setName("Achievement 1");
        
        Achievement achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setName("Achievement 2");

        List<Achievement> achievements = Arrays.asList(achievement1, achievement2);
        when(achievementRepository.findAll()).thenReturn(achievements);

        // Выполнение
        List<Achievement> result = achievementService.getAll();

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(achievement1.getId(), result.get(0).getId());
        assertEquals(achievement2.getId(), result.get(1).getId());
        
        verify(achievementRepository).findAll();
    }

    @Test
    void testGetById() {
        // Подготовка данных
        Long achievementId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(1);
        achievement.setName("Test Achievement");

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        // Выполнение
        Achievement result = achievementService.getById(achievementId);

        // Проверки
        assertNotNull(result);
        assertEquals(achievement.getId(), result.getId());
        assertEquals(achievement.getName(), result.getName());
        
        verify(achievementRepository).findById(achievementId);
    }

    @Test
    void testGetByIdNotFound() {
        // Подготовка данных
        Long achievementId = 999L;
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> achievementService.getById(achievementId));
        
        verify(achievementRepository).findById(achievementId);
    }

    @Test
    void testCreate() {
        // Подготовка данных
        AchievementDTO dto = new AchievementDTO(
            null,
            "New Achievement",
            "Description",
            100,
            "icon.png",
            AchievementType.XP.getValue()
        );

        Achievement achievement = Achievement.fromDTO(dto);
        achievement.setId(1);

        when(achievementRepository.existsByName(dto.name())).thenReturn(false);
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);

        // Выполнение
        AchievementDTO result = achievementService.create(dto);

        // Проверки
        assertNotNull(result);
        assertEquals(dto.name(), result.name());
        assertEquals(dto.description(), result.description());
        assertEquals(dto.requiredXp(), result.requiredXp());
        assertEquals(dto.icon(), result.icon());
        assertEquals(dto.type(), result.type());
        
        verify(achievementRepository).existsByName(dto.name());
        verify(achievementRepository).save(any(Achievement.class));
    }

    @Test
    void testCreateWithExistingName() {
        // Подготовка данных
        AchievementDTO dto = new AchievementDTO(
            null,
            "Existing Achievement",
            "Description",
            100,
            "icon.png",
            AchievementType.XP.getValue()
        );

        when(achievementRepository.existsByName(dto.name())).thenReturn(true);

        // Проверка исключения
        assertThrows(IllegalStateException.class, () -> achievementService.create(dto));
        
        verify(achievementRepository).existsByName(dto.name());
        verify(achievementRepository, never()).save(any(Achievement.class));
    }

    @Test
    void testUpdate() {
        // Подготовка данных
        Long achievementId = 1L;
        AchievementDTO dto = new AchievementDTO(
            1,
            "Updated Achievement",
            "Updated Description",
            200,
            "updated_icon.png",
            AchievementType.TASKS_COMPLETED.getValue()
        );

        Achievement existingAchievement = new Achievement();
        existingAchievement.setId(1);
        existingAchievement.setName("Old Name");

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(existingAchievement));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(existingAchievement);

        // Выполнение
        AchievementDTO result = achievementService.update(achievementId, dto);

        // Проверки
        assertNotNull(result);
        assertEquals(dto.name(), result.name());
        assertEquals(dto.description(), result.description());
        assertEquals(dto.requiredXp(), result.requiredXp());
        assertEquals(dto.icon(), result.icon());
        assertEquals(dto.type(), result.type());
        
        verify(achievementRepository).findById(achievementId);
        verify(achievementRepository).save(existingAchievement);
        verify(userAchievementRepository).findByAchievement(existingAchievement);
    }

    @Test
    void testUpdateNotFound() {
        // Подготовка данных
        Long achievementId = 999L;
        AchievementDTO dto = new AchievementDTO(
            999,
            "Non-existent Achievement",
            "Description",
            100,
            "icon.png",
            AchievementType.XP.getValue()
        );

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> achievementService.update(achievementId, dto));
        
        verify(achievementRepository).findById(achievementId);
        verify(achievementRepository, never()).save(any(Achievement.class));
    }

    @Test
    void testDeleteAchievement() {
        // Подготовка данных
        Long achievementId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(1);

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        // Выполнение
        achievementService.deleteAchievement(achievementId);

        // Проверки
        verify(achievementRepository).findById(achievementId);
        verify(achievementRepository).delete(achievement);
    }

    @Test
    void testDeleteAchievementNotFound() {
        // Подготовка данных
        Long achievementId = 999L;
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> achievementService.deleteAchievement(achievementId));
        
        verify(achievementRepository).findById(achievementId);
        verify(achievementRepository, never()).delete(any(Achievement.class));
    }

    @Test
    void testUpdateWithInvalidUserAchievements() {
        // Подготовка данных
        Long achievementId = 1L;
        AchievementDTO dto = new AchievementDTO(
            1,
            "Updated Achievement",
            "Updated Description",
            200,
            "updated_icon.png",
            AchievementType.TASKS_COMPLETED.getValue()
        );

        Achievement existingAchievement = new Achievement();
        existingAchievement.setId(1);
        existingAchievement.setName("Old Name");

        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        UserAchievement ua1 = new UserAchievement();
        ua1.setUser(user1);
        ua1.setAchievement(existingAchievement);
        UserAchievement ua2 = new UserAchievement();
        ua2.setUser(user2);
        ua2.setAchievement(existingAchievement);

        List<UserAchievement> userAchievements = Arrays.asList(ua1, ua2);

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(existingAchievement));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(existingAchievement);
        when(userAchievementRepository.findByAchievement(existingAchievement)).thenReturn(userAchievements);
        when(detector.checkAchievementRequirements(user1, existingAchievement)).thenReturn(true);
        when(detector.checkAchievementRequirements(user2, existingAchievement)).thenReturn(false);

        // Выполнение
        AchievementDTO result = achievementService.update(achievementId, dto);

        // Проверки
        assertNotNull(result);
        verify(achievementRepository).findById(achievementId);
        verify(achievementRepository).save(existingAchievement);
        verify(userAchievementRepository).findByAchievement(existingAchievement);
        verify(detector).checkAchievementRequirements(user1, existingAchievement);
        verify(detector).checkAchievementRequirements(user2, existingAchievement);
        verify(userAchievementRepository, never()).delete(ua1); // Достижение все еще валидно
        verify(userAchievementRepository).delete(ua2); // Достижение стало невалидным
    }

    @Test
    void testGetUserAchievements() {
        // Подготовка данных
        Long userId = 1L;

        // Выполнение
        List<Achievement> result = achievementService.getUserAchievements(userId);

        // Проверки
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
} 