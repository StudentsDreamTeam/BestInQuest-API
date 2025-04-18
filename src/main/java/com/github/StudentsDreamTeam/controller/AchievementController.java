package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping("/user/{userId}")
    public List<Achievement> getUserAchievements(@PathVariable Long userId) {
        return achievementService.getUserAchievements(userId);
    }

    @GetMapping("/")
    public List<Achievement> getAllAchievements() {
        return achievementService.getAllAchievements();
    }
}
