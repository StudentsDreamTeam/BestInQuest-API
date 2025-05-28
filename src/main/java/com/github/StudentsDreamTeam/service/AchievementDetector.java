package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UserAchievementDTO;
import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UserAchievement;
import com.github.StudentsDreamTeam.repository.AchievementRepository;
import com.github.StudentsDreamTeam.repository.TaskRepository;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementDetector {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final TaskRepository taskRepository;

    public boolean checkAchievementRequirements(User user, Achievement achievement) {
        long comparingValue;

        switch (achievement.getType()) {
            case XP -> comparingValue = user.getXp() != null ? user.getXp() : 0;
            case STREAK -> comparingValue = user.getStreak() != null ? user.getStreak() : 0;
            case TASKS_COMPLETED -> comparingValue = taskRepository.countCompletedByUser(user);
            case INVENTORY_ITEMS -> comparingValue = user.getInventoryItemsCount();
            default -> throw new IllegalArgumentException("Unknown achievement type: " + achievement.getType());
        }

        return comparingValue >= achievement.getRequired_value();
    }

    public List<UserAchievementDTO> detectForUser(User user) {
        List<Achievement> allAchievements = achievementRepository.findAchievementsNotOwnedByUserNative(user.getId());
        List<UserAchievementDTO> newAchievements = new ArrayList<>();

        for (Achievement achievement : allAchievements) {

            if (checkAchievementRequirements(user, achievement)) {
                UserAchievement ua = new UserAchievement();
                ua.setUser(user);
                ua.setAchievement(achievement);
                ua.setAcquireDate(LocalDateTime.now());

                userAchievementRepository.save(ua);
                newAchievements.add(UserAchievementDTO.fromORM(ua));
            }
        }

        return newAchievements;
    }

    public void revertAchievementsForUser(User user) {
        List<UserAchievement> userAchievements = userAchievementRepository.findByUser(user);

        for (UserAchievement ua : userAchievements) {
            if (!checkAchievementRequirements(user, ua.getAchievement())) {
                userAchievementRepository.delete(ua);
            }
        }
    }
}
