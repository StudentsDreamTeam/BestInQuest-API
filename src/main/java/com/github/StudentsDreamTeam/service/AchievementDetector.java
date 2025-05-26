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

    public List<UserAchievementDTO> detectForUser(User user) {
        List<Achievement> allAchievements = achievementRepository.findAll();
        List<UserAchievementDTO> newAchievements = new ArrayList<>();

        for (Achievement achievement : allAchievements) {
            boolean alreadyHas = userAchievementRepository.existsByUserAndAchievement(user, achievement);
            long completedCount = taskRepository.countCompletedByUser(user);
            if (alreadyHas) continue;

            boolean conditionMet = switch (achievement.getType()) {
                case XP -> user.getXp() >= achievement.getRequired_value();
                case STREAK -> user.getStreak() >= achievement.getRequired_value();
                case TASKS_COMPLETED -> completedCount >= achievement.getRequired_value();
                case INVENTORY_ITEMS -> user.getInventoryItems() >= achievement.getRequired_value();
            };

            if (conditionMet) {
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
        long completedCount = taskRepository.countCompletedByUser(user);

        for (UserAchievement ua : userAchievements) {
            Achievement achievement = ua.getAchievement();

            boolean conditionStillMet = switch (achievement.getType()) {
                case XP -> user.getXp() >= achievement.getRequired_value();
                case STREAK -> user.getStreak() >= achievement.getRequired_value();
                case TASKS_COMPLETED -> completedCount >= achievement.getRequired_value();
                case INVENTORY_ITEMS -> user.getInventoryItems() >= achievement.getRequired_value();
            };

            if (!conditionStillMet) {
                userAchievementRepository.delete(ua);
            }
        }
    }

}
