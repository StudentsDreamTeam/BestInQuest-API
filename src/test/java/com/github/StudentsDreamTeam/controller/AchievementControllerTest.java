package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.AchievementDTO;
import com.github.StudentsDreamTeam.dto.UserAchievementDTO;
import com.github.StudentsDreamTeam.enums.AchievementType;
import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UserAchievement;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.service.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @InjectMocks
    private AchievementController achievementController;

    @Test
    void testGetAchievements() {
        // Подготовка данных
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        Achievement achievement1 = new Achievement();
        achievement1.setId(1);
        achievement1.setName("Achievement 1");
        achievement1.setDescription("Description 1");
        achievement1.setType(AchievementType.XP);
        achievement1.setRequiredValue(100);
        achievement1.setIcon("icon1.png");

        Achievement achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setName("Achievement 2");
        achievement2.setDescription("Description 2");
        achievement2.setType(AchievementType.STREAK);
        achievement2.setRequiredValue(7);
        achievement2.setIcon("icon2.png");

        UserAchievement ua1 = new UserAchievement();
        ua1.setId(1);
        ua1.setUser(user);
        ua1.setAchievement(achievement1);
        ua1.setAcquireDate(LocalDateTime.now());

        UserAchievement ua2 = new UserAchievement();
        ua2.setId(2);
        ua2.setUser(user);
        ua2.setAchievement(achievement2);
        ua2.setAcquireDate(LocalDateTime.now());

        List<UserAchievement> userAchievements = Arrays.asList(ua1, ua2);

        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(user));
        when(userAchievementRepository.findByUser(user)).thenReturn(userAchievements);

        // Выполнение
        List<UserAchievementDTO> result = achievementController.getAchievements(userId);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(achievement1.getId(), result.get(0).achievement().id());
        assertEquals(achievement1.getName(), result.get(0).achievement().name());
        assertEquals(achievement2.getId(), result.get(1).achievement().id());
        assertEquals(achievement2.getName(), result.get(1).achievement().name());

        verify(userRepository).findById(Long.valueOf(userId));
        verify(userAchievementRepository).findByUser(user);
    }

    @Test
    void testGetAchievementsUserNotFound() {
        // Подготовка данных
        Integer userId = 999;
        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> achievementController.getAchievements(userId));

        verify(userRepository).findById(Long.valueOf(userId));
        verify(userAchievementRepository, never()).findByUser(any());
    }

    @Test
    void testGetById() {
        // Подготовка данных
        Long achievementId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(1);
        achievement.setName("Test Achievement");
        achievement.setDescription("Test Description");
        achievement.setType(AchievementType.XP);
        achievement.setRequiredValue(100);
        achievement.setIcon("test_icon.png");

        when(achievementService.getById(achievementId)).thenReturn(achievement);

        // Выполнение
        AchievementDTO result = achievementController.getById(achievementId);

        // Проверки
        assertNotNull(result);
        assertEquals(achievement.getId(), result.id());
        assertEquals(achievement.getName(), result.name());
        assertEquals(achievement.getDescription(), result.description());
        assertEquals(achievement.getType().getValue(), result.type());
        assertEquals(achievement.getRequiredValue(), result.requiredXp());
        assertEquals(achievement.getIcon(), result.icon());

        verify(achievementService).getById(achievementId);
    }

    @Test
    void testGetByIdNotFound() {
        // Подготовка данных
        Long achievementId = 999L;
        when(achievementService.getById(achievementId)).thenThrow(new EntityNotFoundException("Achievement not found"));

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> achievementController.getById(achievementId));

        verify(achievementService).getById(achievementId);
    }

    @Test
    void testGetAll() {
        // Подготовка данных
        Achievement achievement1 = new Achievement();
        achievement1.setId(1);
        achievement1.setName("Achievement 1");
        achievement1.setType(AchievementType.XP);

        Achievement achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setName("Achievement 2");
        achievement2.setType(AchievementType.STREAK);

        List<Achievement> achievements = Arrays.asList(achievement1, achievement2);
        when(achievementService.getAll()).thenReturn(achievements);

        // Выполнение
        List<AchievementDTO> result = achievementController.getAll();

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(achievement1.getId(), result.get(0).id());
        assertEquals(achievement1.getName(), result.get(0).name());
        assertEquals(achievement2.getId(), result.get(1).id());
        assertEquals(achievement2.getName(), result.get(1).name());

        verify(achievementService).getAll();
    }

    @Test
    void testCreate() {
        // Подготовка данных
        AchievementDTO inputDto = new AchievementDTO(
            null,
            "New Achievement",
            "Description",
            100,
            "icon.png",
            AchievementType.XP.getValue()
        );

        Achievement savedAchievement = Achievement.fromDTO(inputDto);
        savedAchievement.setId(1);

        when(achievementService.create(any(AchievementDTO.class))).thenReturn(AchievementDTO.fromORM(savedAchievement));

        // Выполнение
        AchievementDTO result = achievementController.create(inputDto);

        // Проверки
        assertNotNull(result);
        assertEquals(savedAchievement.getId(), result.id());
        assertEquals(inputDto.name(), result.name());
        assertEquals(inputDto.description(), result.description());
        assertEquals(inputDto.type(), result.type());
        assertEquals(inputDto.requiredXp(), result.requiredXp());
        assertEquals(inputDto.icon(), result.icon());

        verify(achievementService).create(any(AchievementDTO.class));
    }

    @Test
    void testUpdate() {
        // Подготовка данных
        Long achievementId = 1L;
        AchievementDTO inputDto = new AchievementDTO(
            achievementId.intValue(),
            "Updated Achievement",
            "Updated Description",
            200,
            "updated_icon.png",
            AchievementType.TASKS_COMPLETED.getValue()
        );

        Achievement updatedAchievement = Achievement.fromDTO(inputDto);
        updatedAchievement.setId(achievementId.intValue());

        when(achievementService.update(eq(achievementId), any(AchievementDTO.class)))
            .thenReturn(AchievementDTO.fromORM(updatedAchievement));

        // Выполнение
        AchievementDTO result = achievementController.update(achievementId, inputDto);

        // Проверки
        assertNotNull(result);
        assertEquals(updatedAchievement.getId(), result.id());
        assertEquals(inputDto.name(), result.name());
        assertEquals(inputDto.description(), result.description());
        assertEquals(inputDto.type(), result.type());
        assertEquals(inputDto.requiredXp(), result.requiredXp());
        assertEquals(inputDto.icon(), result.icon());

        verify(achievementService).update(eq(achievementId), any(AchievementDTO.class));
    }

    @Test
    void testUpdateNotFound() {
        // Подготовка данных
        Long achievementId = 999L;
        AchievementDTO inputDto = new AchievementDTO(
            achievementId.intValue(),
            "Non-existent Achievement",
            "Description",
            100,
            "icon.png",
            AchievementType.XP.getValue()
        );

        when(achievementService.update(eq(achievementId), any(AchievementDTO.class)))
            .thenThrow(new EntityNotFoundException("Achievement not found"));

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> achievementController.update(achievementId, inputDto));

        verify(achievementService).update(eq(achievementId), any(AchievementDTO.class));
    }

    @Test
    void testDelete() {
        // Подготовка данных
        Long achievementId = 1L;

        // Выполнение
        achievementController.delete(achievementId);

        // Проверки
        verify(achievementService).deleteAchievement(achievementId);
    }

    @Test
    void testDeleteNotFound() {
        // Подготовка данных
        Long achievementId = 999L;
        doThrow(new EntityNotFoundException("Achievement not found"))
            .when(achievementService).deleteAchievement(achievementId);

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> achievementController.delete(achievementId));

        verify(achievementService).deleteAchievement(achievementId);
    }

    @Test
    void testCreate_WithExistingName() {
        // Подготовка данных
        AchievementDTO inputDto = new AchievementDTO(
            null,
            "Existing Achievement",
            "Description",
            100,
            "icon.png",
            AchievementType.XP.getValue()
        );

        when(achievementService.create(any(AchievementDTO.class)))
            .thenThrow(new IllegalStateException("Achievement with this name already exists"));

        // Проверка исключения
        assertThrows(IllegalStateException.class, () -> achievementController.create(inputDto));

        verify(achievementService).create(any(AchievementDTO.class));
    }

    @Test
    void testUpdate_WithExistingName() {
        // Подготовка данных
        Long achievementId = 1L;
        AchievementDTO inputDto = new AchievementDTO(
            achievementId.intValue(),
            "Existing Achievement",
            "Description",
            100,
            "icon.png",
            AchievementType.XP.getValue()
        );

        when(achievementService.update(eq(achievementId), any(AchievementDTO.class)))
            .thenThrow(new IllegalStateException("Achievement with this name already exists"));

        // Проверка исключения
        assertThrows(IllegalStateException.class, () -> achievementController.update(achievementId, inputDto));

        verify(achievementService).update(eq(achievementId), any(AchievementDTO.class));
    }

    @Test
    void testGetAll_EmptyList() {
        // Подготовка данных
        when(achievementService.getAll()).thenReturn(List.of());

        // Выполнение
        List<AchievementDTO> result = achievementController.getAll();

        // Проверки
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(achievementService).getAll();
    }

    @Test
    void testGetAchievements_EmptyList() {
        // Подготовка данных
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(user));
        when(userAchievementRepository.findByUser(user)).thenReturn(List.of());

        // Выполнение
        List<UserAchievementDTO> result = achievementController.getAchievements(userId);

        // Проверки
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository).findById(Long.valueOf(userId));
        verify(userAchievementRepository).findByUser(user);
    }
} 