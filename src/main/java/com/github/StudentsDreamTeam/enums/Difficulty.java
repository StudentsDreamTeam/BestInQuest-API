package com.github.StudentsDreamTeam.enums;

public enum Difficulty {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private final int level;

    Difficulty(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static Difficulty fromValue(int value) {
        for (Difficulty d : values()) {
            if (d.level == value) return d;
        }
        throw new IllegalArgumentException("Unknown difficulty: " + value);
    }
}
