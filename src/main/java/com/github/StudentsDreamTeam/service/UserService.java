package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UpdateUserProfileDTO;
import com.github.StudentsDreamTeam.model.LevelRequirement;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.LevelRequirementRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LevelRequirementRepository levelRequirementRepository;

    @Transactional
    public User updateUserProfile(Integer userId, UpdateUserProfileDTO dto) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (dto.name() != null) user.setName(dto.name());
        if (dto.email() != null && !dto.email().equalsIgnoreCase(user.getEmail())) {
            boolean emailTaken = userRepository.existsByEmail(dto.email());
            if (emailTaken) {
                throw new IllegalStateException("Email is already in use: " + dto.email());
            }
            user.setEmail(dto.email());
        }
        if (dto.password() != null) user.setPassword(dto.password());

        return userRepository.save(user);
    }

    public void updateUserLevel(User user) {
        List<LevelRequirement> matchedLevels = levelRequirementRepository.findAllByXp(user.getXp());
        if (!matchedLevels.isEmpty()) {
            int newLevel = matchedLevels.get(0).getLevel();
            if (!Objects.equals(user.getLevel(), newLevel)) {
                user.setLevel(newLevel);
                userRepository.save(user);
            }
        }
    }

    public Integer getXpToNextLevel(User user) {
        return levelRequirementRepository.findNextLevel(user.getLevel())
                .map(next -> next.getRequiredXp() - user.getXp())
                .orElse(0);
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already in use.");
        }
        
        return userRepository.save(user);
    }

    private void updateLoginActivity(User user) {
        LocalDate today = LocalDate.now();
        LocalDate lastOnline = user.getLastInDate() != null ? user.getLastInDate().toLocalDate() : null;

        if (lastOnline == null) {
            user.setStreak(1);
        } else if (lastOnline.equals(today)) {
            return;
        } else if (lastOnline.plusDays(1).equals(today)) {
            user.setStreak(user.getStreak() != null ? user.getStreak() + 1 : 1);
        } else {
            user.setStreak(1);
        }

        user.setLastInDate(LocalDateTime.now());
    }

    @Transactional
    public User getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        updateLoginActivity(user);
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
