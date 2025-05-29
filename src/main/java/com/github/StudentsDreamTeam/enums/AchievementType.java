package com.github.StudentsDreamTeam.enums;

public enum AchievementType {
    XP("xp"),
    TASKS_COMPLETED("tasks_completed"),
    STREAK("streak"),
    INVENTORY_ITEMS("inventory_items");

    private final String value;

    AchievementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AchievementType fromValue(String value) {
        for (AchievementType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown achievement type: " + value);
    }
}
