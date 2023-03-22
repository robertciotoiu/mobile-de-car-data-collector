package com.robertciotoiu.service;

public enum RunningMode {
    FULL,
    NEW_ONLY;

    public static RunningMode fromString(String modeString) {
        for (RunningMode mode : RunningMode.values()) {
            if (mode.name().equalsIgnoreCase(modeString)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid running mode: " + modeString);
    }
}
