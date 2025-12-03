package com.comp2042;

/**
 * Central configuration class for all game constants.
 * All variables, values and configurable ints are to be defined here.
 */
public final class GameConfig {

    // -------------------- Board Configuration --------------------
    /** Width of the game board in cells */
    public static final int BOARD_WIDTH = 25;

    /** Height of the game board in cells */
    public static final int BOARD_HEIGHT = 10;

    /** Starting X position for new tetrominos */
    public static final int TETROMINO_START_X = 4;

    /** Starting Y position for new tetrominos */
    public static final int TETROMINO_START_Y = 10;

    // -------------------- Lock Delay Settings --------------------
    /** Duration in milliseconds before a piece locks when it can't move down */
    public static final int LOCK_DELAY_MS = 300;

    /** Maximum number of times lock delay can be reset by moving/rotating */
    public static final int MAX_LOCK_RESETS = 10;

    /** Whether lock delay feature is enabled */
    public static final boolean LOCK_DELAY_ENABLED = true;

    // -------------------- Rendering Configuration --------------------
    /** Size of each brick cell in pixels */
    public static final int BRICK_SIZE = 20;

    /** Number of top rows to skip for spawn area (not displayed) */
    public static final int BOARD_ROW_OFFSET = 2;

    /** Vertical offset for brick panel positioning */
    public static final int BRICK_PANEL_Y_OFFSET = -42;

    /** Opacity of ghost tetromino preview (0.0 - 1.0) */
    public static final double GHOST_OPACITY = 0.70;

    // -------------------- Game Timing --------------------
    /** Auto-drop interval in milliseconds (how fast pieces fall) */
    public static final int GAME_TICK_DURATION_MS = 400;

    // -------------------- Scoring Configuration --------------------
    /** Score multiplier per row cleared (score = multiplier * rows * rows) */
    public static final int SCORE_MULTIPLIER_PER_ROW = 50;

    // -------------------- UI Configuration --------------------
    /** Font size for digital display font */
    public static final int DIGITAL_FONT_SIZE = 38;

    /** Minimum height of notification panel */
    public static final int NOTIFICATION_PANEL_MIN_HEIGHT = 200;

    /** Minimum width of notification panel */
    public static final int NOTIFICATION_PANEL_MIN_WIDTH = 220;

    /** Glow effect intensity for notifications (0.0 - 1.0) */
    public static final double NOTIFICATION_GLOW_INTENSITY = 0.6;

    /** Fade animation duration for notifications in milliseconds */
    public static final int NOTIFICATION_FADE_DURATION_MS = 2000;

    /** Translate animation duration for notifications in milliseconds */
    public static final int NOTIFICATION_TRANSLATE_DURATION_MS = 2500;

    /** Vertical distance notification moves upward in pixels */
    public static final int NOTIFICATION_MOVE_DISTANCE = 40;

    // -------------------- Visual Effects Configuration --------------------
    /** Reflection effect fraction (unused currently) */
    public static final double REFLECTION_FRACTION = 0.8;

    /** Reflection effect top opacity (unused currently) */
    public static final double REFLECTION_TOP_OPACITY = 0.9;

    /** Reflection effect top offset (unused currently) */
    public static final int REFLECTION_TOP_OFFSET = -12;

    // -------------------- Window Configuration --------------------
    /** Initial window width in pixels */
    public static final int WINDOW_WIDTH = 300;

    /** Initial window height in pixels */
    public static final int WINDOW_HEIGHT = 510;

    // Prevent instantiation
    private GameConfig() {
        throw new UnsupportedOperationException("Utility class - do not instantiate");
    }
}
