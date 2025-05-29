package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.AuthDTO;
import com.github.StudentsDreamTeam.dto.UpdateUserProfileDTO;
import com.github.StudentsDreamTeam.dto.UserAchievementDTO;
import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.UserAchievement;
import com.github.StudentsDreamTeam.repository.UserAchievementRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import com.github.StudentsDreamTeam.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDTO getUserProfile(@PathVariable Long id) {
        return UserDTO.fromORM(userService.getUserProfile(id));
    }

    @GetMapping("/all")
    public List<UserDTO> getAll() {
        return userService.getAll().stream().map(UserDTO::fromORM).toList();
    }

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }

    @PostMapping("/wrong-add")
    public UserDTO testAdd(@RequestBody UserDTO userDTO) {
        User user = User.fromDTO(userDTO);

        return UserDTO.fromORM(userService.registerUser(user));
    }

    @PostMapping("/auth")
    public UserDTO auth(@RequestBody AuthDTO authDTO) {
        List<User> users = userService.getAll();
        for (User user : users) {
            if (authDTO.email().equalsIgnoreCase(user.getEmail())) {
                if (authDTO.password().equals(user.getPassword())) {
                    return UserDTO.fromORM(user);
                }
                else {
                    throw new SecurityException("Incorrect login or password.");
                }
            }
        }
        throw new SecurityException("Incorrect login or password.");
    }

    @PutMapping("/update-profile/{userId}")
    public UserDTO updateProfile(
            @PathVariable Integer userId,
            @RequestBody UpdateUserProfileDTO dto
    ) {
        User updated = userService.updateUserProfile(userId, dto);
        return (UserDTO.fromORM(updated));
    }
}
