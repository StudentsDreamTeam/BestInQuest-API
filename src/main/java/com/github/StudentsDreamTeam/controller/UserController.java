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
    public UserDTO getUserProfile(@PathVariable Long id) {
        return UserDTO.fromORM(userService.getUserProfile(id));
    }

    @GetMapping("/all")
    public List<UserDTO> getAll() {
        return userService.getAll().stream().map(UserDTO::fromORM).toList();
    }

    @PostMapping("/wrong-add")
    public UserDTO testAdd(@RequestBody UserDTO userDTO) {
        User user = User.fromDTO(userDTO);

        return UserDTO.fromORM(userService.addUser(user));
    }
}
