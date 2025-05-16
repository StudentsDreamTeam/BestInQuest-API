package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.Shop;

public record ShopDTO(
        Integer id,
        Integer itemId,
        Long cost,
        String availability
) {
    public static ShopDTO fromORM(Shop shop) {
        return new ShopDTO(
                shop.getId(),
                shop.getItem().getId(),
                shop.getCost(),
                shop.getAvailability().getValue()
        );
    }
}