package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.Item;
import com.github.StudentsDreamTeam.model.Task;

import java.time.Duration;

public record ItemDTO(
        Integer id,
        String name,
        String description,
        String rarity,
        Long xpMultiplier,
        Long currencyMultiplier,
        Long duration,
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
                item.getDuration() != null ? item.getDuration().getSeconds() : null,
                item.getCost()
        );
    }
}
