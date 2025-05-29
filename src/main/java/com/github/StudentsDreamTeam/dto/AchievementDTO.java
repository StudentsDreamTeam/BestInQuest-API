package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.Achievement;

public record AchievementDTO(Integer id,
                             String name,
                             String description,
                             Integer requiredXp,
                             String icon,
                             String type) {
    public static AchievementDTO fromORM(Achievement achievement) {
        return new AchievementDTO(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getRequiredValue(),
                achievement.getIcon(),
                achievement.getType() != null ? achievement.getType().getValue() : null
        );
    }
}
