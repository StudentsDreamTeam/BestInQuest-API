package com.github.StudentsDreamTeam.enums;

public enum Difficulty {
    VERY_EASY(0),
    EASY(1),
    NORMAL(2),
    HARD(3),
    VERY_HARD(4);

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
