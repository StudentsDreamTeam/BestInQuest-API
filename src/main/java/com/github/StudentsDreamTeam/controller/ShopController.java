package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import com.github.StudentsDreamTeam.dto.ShopDTO;
import com.github.StudentsDreamTeam.service.ItemService;
import com.github.StudentsDreamTeam.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping
    public List<ShopDTO> getAllItems() {
        return shopService.getAllShopItems();
    }

    @PostMapping
    public ShopDTO addToShop(@RequestBody ShopDTO dto) {
        return shopService.addToShop(dto);
    }

    @DeleteMapping("/{itemId}")
    public void removeFromShop(@PathVariable Integer itemId) {
        shopService.removeFromShop(itemId);
    }

    @PutMapping("/{itemId}/price")
    public ShopDTO updatePrice(
            @PathVariable Integer itemId,
            @RequestParam Long newCost
    ) {
        return shopService.updatePrice(itemId, newCost);
    }
}
