package com.github.StudentsDreamTeam.enums;

public enum Availability {
    AVAILABLE("available"),
    LIMITED("limited"),
    OUT_OF_STOCK("out_of_stock");

    private final String value;

    Availability(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Availability fromValue(String value) {
        for (Availability status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown availability status: " + value);
    }
}
