package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.ItemDTO;
import jakarta.persistence.*;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "items")
@Data
@Getter
@Setter

public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "rarity", nullable = false)
    private String rarity;

    @Column(name = "xp_multiplier", nullable = false)
    private Long xpMultiplier;

    @Column(name = "currency_multiplier", nullable = false)
    private Long currencyMultiplier;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "cost", nullable = false)
    private Long cost;

    public static Item fromDTO(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.id());
        item.setName(itemDTO.name());
        item.setDescription(itemDTO.description());
        item.setRarity(itemDTO.rarity());
        item.setXpMultiplier(itemDTO.xpMultiplier());
        item.setCurrencyMultiplier(itemDTO.currencyMultiplier());
        item.setDuration(itemDTO.duration());
        item.setCost(itemDTO.cost());
        return item;
    }
}
