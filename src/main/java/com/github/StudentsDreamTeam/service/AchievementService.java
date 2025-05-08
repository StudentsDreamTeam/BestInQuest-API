package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    public List<Achievement> getUserAchievements(Long userId) {
//        return achievementRepository.findByUserId(userId);
        return List.of();
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    public Achievement getAchievement(Long achievementId) {
        return achievementRepository.findById(achievementId).orElseThrow(() -> new EntityNotFoundException("Achievement not found"));
    }
}
