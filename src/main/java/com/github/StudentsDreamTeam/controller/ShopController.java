package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.ShopDTO;
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
    public ResponseEntity<List<ShopDTO>> getAllItems() {
        return ResponseEntity.ok(shopService.getAllShopItems());
    }

    @PostMapping
    public ResponseEntity<ShopDTO> addToShop(@RequestBody ShopDTO dto) {
        return ResponseEntity.ok(shopService.addToShop(dto));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeFromShop(@PathVariable Integer itemId) {
        shopService.removeFromShop(itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{itemId}/price")
    public ResponseEntity<ShopDTO> updatePrice(
            @PathVariable Integer itemId,
            @RequestParam Long newCost
    ) {
        return ResponseEntity.ok(shopService.updatePrice(itemId, newCost));
    }
}
