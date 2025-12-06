package com.comp2042.manager;

import com.comp2042.model.settings.DifficultyMode;
import com.comp2042.model.settings.KeyBindings;
import javafx.beans.property.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.util.Properties;

/**
 * Singleton class managing all game settings.
 * Handles music, difficulty, keybindings, and persistence.
 */
public class SettingsManager {

    private static SettingsManager instance;
    private static final String SETTINGS_FILE = "game_settings.properties";

    // Observable properties
    private final BooleanProperty musicMuted = new SimpleBooleanProperty(false);
    private final ObjectProperty<DifficultyMode> difficulty = new SimpleObjectProperty<>(DifficultyMode.NORMAL);
    private final KeyBindings keyBindings = new KeyBindings();

    private SettingsManager() {
        loadSettings();
        setupListeners();
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    private void setupListeners() {
        // Auto-mute/unmute music when property changes
        musicMuted.addListener((obs, oldVal, newVal) -> {
            try {
                AudioManager audioManager = AudioManager.getInstance();
                if (newVal) {
                    audioManager.setVolume(0.0);
                } else {
                    audioManager.setVolume(0.5);
                }
            } catch (Exception e) {
                // AudioManager might not be initialized yet
            }
            saveSettings();
        });

        // Save difficulty changes
        difficulty.addListener((obs, oldVal, newVal) -> {
            saveSettings();
        });
    }

    /**
     * Loads settings from file.
     */
    private void loadSettings() {
        try {
            File settingsFile = new File(SETTINGS_FILE);
            if (settingsFile.exists()) {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(settingsFile)) {
                    props.load(fis);
                }

                // Load music mute
                musicMuted.set(Boolean.parseBoolean(props.getProperty("musicMuted", "false")));

                // Load difficulty
                String diffStr = props.getProperty("difficulty", "NORMAL");
                try {
                    difficulty.set(DifficultyMode.valueOf(diffStr));
                } catch (IllegalArgumentException e) {
                    difficulty.set(DifficultyMode.NORMAL);
                }

                // Load keybindings
                for (KeyBindings.Action action : KeyBindings.Action.values()) {
                    String keyName = props.getProperty("key." + action.name());
                    if (keyName != null) {
                        try {
                            KeyCode keyCode = KeyCode.valueOf(keyName);
                            keyBindings.setBinding(action, keyCode);
                        } catch (IllegalArgumentException e) {
                            // Keep default
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
        }
    }

    /**
     * Saves settings to file.
     */
    public void saveSettings() {
        try {
            Properties props = new Properties();
            props.setProperty("musicMuted", String.valueOf(musicMuted.get()));
            props.setProperty("difficulty", difficulty.get().name());

            // Save keybindings
            for (KeyBindings.Action action : KeyBindings.Action.values()) {
                KeyCode keyCode = keyBindings.getBinding(action);
                if (keyCode != null) {
                    props.setProperty("key." + action.name(), keyCode.name());
                }
            }

            try (FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)) {
                props.store(fos, "Tetris Game Settings");
            }
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    // Property getters
    public BooleanProperty musicMutedProperty() {
        return musicMuted;
    }

    public ObjectProperty<DifficultyMode> difficultyProperty() {
        return difficulty;
    }

    public KeyBindings getKeyBindings() {
        return keyBindings;
    }

    // Convenience getters/setters
    public boolean isMusicMuted() {
        return musicMuted.get();
    }

    public void setMusicMuted(boolean muted) {
        musicMuted.set(muted);
    }

    public DifficultyMode getDifficulty() {
        return difficulty.get();
    }

    public void setDifficulty(DifficultyMode mode) {
        difficulty.set(mode);
    }
}
