package com.github.StudentsDreamTeam.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelRequirementTest {

    @Test
    void testLevelRequirementCreation() {
        LevelRequirement levelRequirement = new LevelRequirement();
        assertNotNull(levelRequirement);
    }

    @Test
    void testLevelRequirementSettersAndGetters() {
        LevelRequirement levelRequirement = new LevelRequirement();

        levelRequirement.setLevel(5); // level is the primary key
        levelRequirement.setRequiredXp(1000);

        assertEquals(5, levelRequirement.getLevel());
        assertEquals(1000, levelRequirement.getRequiredXp());
    }

    @Test
    void testLevelRequirementValidation() {
        // Test data based on actual values from BestInQuestDB.sql
        LevelRequirement levelRequirement = new LevelRequirement();
        
        // Level 1 requires 0 XP
        levelRequirement.setLevel(1);
        levelRequirement.setRequiredXp(0);
        assertEquals(1, levelRequirement.getLevel());
        assertEquals(0, levelRequirement.getRequiredXp());
        
        // Level 2 requires 100 XP
        levelRequirement.setLevel(2);
        levelRequirement.setRequiredXp(100);
        assertEquals(2, levelRequirement.getLevel());
        assertEquals(100, levelRequirement.getRequiredXp());
        
        // Level 3 requires 300 XP
        levelRequirement.setLevel(3);
        levelRequirement.setRequiredXp(300);
        assertEquals(3, levelRequirement.getLevel());
        assertEquals(300, levelRequirement.getRequiredXp());
    }
} 