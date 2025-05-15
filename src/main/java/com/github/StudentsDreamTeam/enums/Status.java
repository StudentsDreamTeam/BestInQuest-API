package com.github.StudentsDreamTeam.enums;

public enum Status {
    NEW("new"),
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    WAITING_REVIEW("waiting_review"),
    DONE("done");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status fromValue(String value) {
        for (Status s : values()) {
            if (s.value.equalsIgnoreCase(value)) return s;
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
