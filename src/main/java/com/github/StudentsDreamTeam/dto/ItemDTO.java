package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.Task;

public record ItemDTO(
        Integer id,
        String name,
        String description,
        String rarity,
        Long xpMultiplier,
        Long currencyMultiplier,
        String duration,
        Long cost
) {
    public static ItemDTO fromORM(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getRarity(),
                item.getXpMultiplier(),
                item.getCurrencyMultiplier(),
                item.getDuration(),
                item.getCost()
        );
    }
}
