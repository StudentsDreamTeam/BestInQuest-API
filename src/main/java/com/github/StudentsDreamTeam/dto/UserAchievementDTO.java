package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.UserAchievement;

import java.time.LocalDateTime;

public record UserAchievementDTO(AchievementDTO achievement,
                                 LocalDateTime acquireDate) {
    public static UserAchievementDTO fromORM(UserAchievement ua) {
        return new UserAchievementDTO(
                AchievementDTO.fromORM(ua.getAchievement()),
                ua.getAcquireDate()
        );
    }
}
