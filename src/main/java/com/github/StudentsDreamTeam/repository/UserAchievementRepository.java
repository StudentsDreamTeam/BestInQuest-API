package com.github.StudentsDreamTeam.repository;

import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer> {
    List<UserAchievement> findByUser(User user);
    boolean existsByUserAndAchievement(User user, Achievement achievement);
}
