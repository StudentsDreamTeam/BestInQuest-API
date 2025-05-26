package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.UpdateUserProfileDTO;
import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.service.UserService;
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

    @PutMapping("/profile/{userId}")
    public UserDTO updateProfile(
            @PathVariable Integer userId,
            @RequestBody UpdateUserProfileDTO dto
    ) {
        User updated = userService.updateUserProfile(userId, dto);
        return (UserDTO.fromORM(updated));
    }

}
