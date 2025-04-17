package com.spd.service;

import com.spd.model.Achievement;
import com.spd.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    public List<Achievement> getUserAchievements(Long userId) {
        return achievementRepository.findByUserId(userId);
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    public Achievement getAchievement(Long achievementId) {
        return achievementRepository.findById(achievementId).orElseThrow(() -> new RuntimeException("Achievement not found"));
    }
}
