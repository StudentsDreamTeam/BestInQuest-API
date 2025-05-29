package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.AchievementDTO;
import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.enums.AchievementType;
import com.github.StudentsDreamTeam.model.Achievement;
import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UserAchievement;
import com.github.StudentsDreamTeam.repository.AchievementRepository;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private AchievementDetector detector;

    public List<Achievement> getUserAchievements(Long userId) {
//        return achievementRepository.findByUserId(userId);
        return List.of();
    }

    public List<Achievement> getAll() {
        return achievementRepository.findAll();
    }

    public Achievement getById(Long achievementId) {
        return achievementRepository.findById(achievementId).orElseThrow(() -> new EntityNotFoundException("Achievement not found"));
    }

    @Transactional
    public AchievementDTO create(AchievementDTO dto) {
        if (achievementRepository.existsByName(dto.name())) {
            throw new IllegalStateException("Achievement with this name already exists");
        }
        Achievement saved = achievementRepository.save(Achievement.fromDTO(dto));
        return AchievementDTO.fromORM(saved);
    }

    @Transactional
    public AchievementDTO update(Long id, AchievementDTO dto) {
        Achievement existing = achievementRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Achievement not found: " + id));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setType(AchievementType.fromValue(dto.type()));
        existing.setRequired_value(dto.requiredXp());
        existing.setIcon(dto.icon());

        Achievement updated = achievementRepository.save(existing);
        removeInvalidUserAchievements(updated);

        return AchievementDTO.fromORM(updated);
    }

    @Transactional
    public void deleteAchievement(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found"));

        achievementRepository.delete(achievement);
    }

    private void removeInvalidUserAchievements(Achievement achievement) {
        List<UserAchievement> userAchievements = userAchievementRepository.findByAchievement(achievement);

        for (UserAchievement ua : userAchievements) {
            User user = ua.getUser();

            boolean stillValid = detector.checkAchievementRequirements(user, achievement);
            if (!stillValid) {
                userAchievementRepository.delete(ua);
            }
        }
    }
}
