package com.comp2042;

// Game config constants.
public final class GameConfig {

    // Lock Delay Settings
    public static final int LOCK_DELAY_MS = 300;
    public static final int MAX_LOCK_RESETS = 10;
    public static final boolean LOCK_DELAY_ENABLED = true;

    // Prevent instantiation
    private GameConfig() {
        throw new UnsupportedOperationException("Utility class");
    }
}
