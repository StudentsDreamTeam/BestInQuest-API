package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.dto.UsersInventoryDTO;
import com.github.StudentsDreamTeam.service.TaskService;
import com.github.StudentsDreamTeam.service.UsersInventoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class UsersInventoryController {

    @Autowired
    private UsersInventoryService service;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public UsersInventoryDTO addItem(@RequestBody UsersInventoryDTO dto) {
        return service.addItemToInventory(dto);
    }

    @DeleteMapping("/{userId}/{itemId}")
    public void removeItem(@PathVariable Integer userId, @PathVariable Integer itemId) {
        service.removeItemFromInventory(userId, itemId);
    }

    @GetMapping
    public List<UsersInventoryDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UsersInventoryDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping("/user/{userId}")
    public List<UsersInventoryDTO> getInventoryByUserId(@PathVariable Integer userId) {
        return service.getInventoryByUserId(userId);
    }

    @DeleteMapping("/sell/{userId}/{itemId}")
    public void sellItem(@PathVariable Integer userId, @PathVariable Integer itemId) {
            service.sellItemFromInventory(userId, itemId);
    }
}
