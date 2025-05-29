package com.github.StudentsDreamTeam.enums;

public enum ItemRarity {
    COMMON("common"),
    RARE("rare"),
    EPIC("epic"),
    LEGENDARY("legendary");

    private final String value;

    ItemRarity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ItemRarity fromValue(String value) {
        for (ItemRarity p : values()) {
            if (p.value.equalsIgnoreCase(value)) return p;
        }
        throw new IllegalArgumentException("Unknown item rarity: " + value);
    }

}
