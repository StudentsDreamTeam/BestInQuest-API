package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
