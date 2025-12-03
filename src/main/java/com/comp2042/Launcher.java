package com.comp2042;

/**
 * Launcher class for the application.
 * Used to bypass JavaFX runtime module issues by not extending Application.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}