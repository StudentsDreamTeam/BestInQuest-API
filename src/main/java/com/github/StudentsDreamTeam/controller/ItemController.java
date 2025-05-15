package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private  ItemService itemService;

    @PostMapping
    public ItemDTO create(@RequestBody ItemDTO dto) {
        return itemService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        itemService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ItemDTO get(@PathVariable Integer id) {
        return itemService.getById(id);
    }

    @GetMapping
    public List<ItemDTO> getAll() {
        return itemService.getAll();
    }
}
