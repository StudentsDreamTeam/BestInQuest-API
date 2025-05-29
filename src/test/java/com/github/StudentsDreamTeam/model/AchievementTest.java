package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.AchievementDTO;
import com.github.StudentsDreamTeam.enums.AchievementType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AchievementTest {

    @Test
    void testAchievementCreation() {
        Achievement achievement = new Achievement();
        assertNotNull(achievement);
    }

    @Test
    void testAchievementSettersAndGetters() {
        Achievement achievement = new Achievement();
        List<UserAchievement> userAchievements = new ArrayList<>();

        achievement.setId(1);
        achievement.setName("Test Achievement");
        achievement.setDescription("Test Description");
        achievement.setRequiredValue(100);
        achievement.setIcon("test_icon.png");
        achievement.setType(AchievementType.XP);
        achievement.setUserAchievements(userAchievements);

        assertEquals(1, achievement.getId());
        assertEquals("Test Achievement", achievement.getName());
        assertEquals("Test Description", achievement.getDescription());
        assertEquals(100, achievement.getRequiredValue());
        assertEquals("test_icon.png", achievement.getIcon());
        assertEquals(AchievementType.XP, achievement.getType());
        assertEquals(userAchievements, achievement.getUserAchievements());
    }

    @Test
    void testFromDTO() {
        AchievementDTO achievementDTO = new AchievementDTO(
            1,
            "Test Achievement",
            "Test Description",
            100,
            "test_icon.png",
            AchievementType.TASKS_COMPLETED.getValue()
        );

        Achievement achievement = Achievement.fromDTO(achievementDTO);

        assertNotNull(achievement);
        assertEquals(achievementDTO.name(), achievement.getName());
        assertEquals(achievementDTO.description(), achievement.getDescription());
        assertEquals(AchievementType.TASKS_COMPLETED, achievement.getType());
        assertEquals(achievementDTO.requiredXp(), achievement.getRequiredValue());
        assertEquals(achievementDTO.icon(), achievement.getIcon());
    }

    @Test
    void testFromDTOWithNullType() {
        AchievementDTO achievementDTO = new AchievementDTO(
            1,
            "Test Achievement",
            "Test Description",
            100,
            "test_icon.png",
            null
        );

        Achievement achievement = Achievement.fromDTO(achievementDTO);

        assertNotNull(achievement);
        assertEquals(achievementDTO.name(), achievement.getName());
        assertEquals(achievementDTO.description(), achievement.getDescription());
        assertEquals(AchievementType.XP, achievement.getType()); // Default type
        assertEquals(achievementDTO.requiredXp(), achievement.getRequiredValue());
        assertEquals(achievementDTO.icon(), achievement.getIcon());
    }
} 