package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.User;

import java.time.LocalDateTime;
import java.util.Date;

public record UserDTO(Integer id, String name, String email, String password, Integer xp,
                      Integer level, LocalDateTime registrationDate, LocalDateTime lastInDate, Integer streak) {
    public static UserDTO fromORM(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(),
                user.getXp(), user.getLevel(), user.getRegistrationDate(), user.getLastInDate(), user.getStreak());
    }
}
