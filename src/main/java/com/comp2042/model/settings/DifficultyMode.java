package com.comp2042.model.settings;

/**
 * Enum representing different difficulty levels.
 * Each difficulty affects spawn height and game tick rate.
 */
public enum DifficultyMode {
    NORMAL("Normal", 2, 400), // Baseline: 400ms tick, spawn at Y=2 (Top Bar is Y=0)
    HARD("Hard", 5, 275), // -125ms tick, spawn at Y=5
    BLITZ("Blitz", 10, 125); // -275ms tick, spawn at Y=10

    private final String displayName;
    private final int spawnYPosition;
    private final int tickDurationMs;

    DifficultyMode(String displayName, int spawnYPosition, int tickDurationMs) {
        this.displayName = displayName;
        this.spawnYPosition = spawnYPosition;
        this.tickDurationMs = tickDurationMs;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSpawnYPosition() {
        return spawnYPosition;
    }

    public int getTickDurationMs() {
        return tickDurationMs;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
