package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.*;
import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UserAchievement;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.service.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @GetMapping("/user/{userId}")
    public List<UserAchievementDTO> getAchievements(@PathVariable Integer userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<UserAchievement> achievements = userAchievementRepository.findByUser(user);
        return achievements.stream()
                .map(UserAchievementDTO::fromORM)
                .toList();
    }

    @GetMapping("/{id}")
    public AchievementDTO getById(@PathVariable Long id) {
        Achievement achievement = achievementService.getById(id);
        return AchievementDTO.fromORM(achievement);
    }

    @GetMapping("/all")
    public List<AchievementDTO> getAll() {
        return achievementService.getAll().stream().map(AchievementDTO::fromORM).toList();
    }

    @PostMapping
    public AchievementDTO create(@RequestBody AchievementDTO dto) {
        return achievementService.create(dto);
    }

    @PutMapping("/{id}")
    public AchievementDTO update(@PathVariable Long id, @RequestBody AchievementDTO dto) {
        return achievementService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
    }
}
