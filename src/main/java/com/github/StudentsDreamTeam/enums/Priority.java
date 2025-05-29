package com.github.StudentsDreamTeam.enums;

public enum Priority {
    OPTIONAL("optional"),
    LOW("low"),
    NORMAL("normal"),
    HIGH("high"),
    CRITICAL("critical");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Priority fromValue(String value) {
        for (Priority p : values()) {
            if (p.value.equalsIgnoreCase(value)) return p;
        }
        throw new IllegalArgumentException("Unknown priority: " + value);
    }
}
