package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User getUserProfile(@PathVariable Long id) {
        return userService.getUserProfile(id);
    }

    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping("/wrong-add")
    public UserDTO testAdd(@RequestBody UserDTO userDTO) {
        User user = User.fromDTO(userDTO);

        return UserDTO.fromORM(userService.addUser(user));
    }
}
