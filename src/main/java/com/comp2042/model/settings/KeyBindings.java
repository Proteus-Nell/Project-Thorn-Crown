package com.comp2042.model.settings;

import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages key bindings for game controls.
 * VALIDATION POINT: Default bindings and dynamic lookup
 */
public class KeyBindings {

    public enum Action {
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_DOWN,
        ROTATE,
        FAST_DROP,
        PAUSE
    }

    private Map<Action, KeyCode> bindings;

    public KeyBindings() {
        bindings = new HashMap<>();
        loadDefaults();
    }

    /**
     * Loads default key bindings.
     */
    private void loadDefaults() {
        bindings.put(Action.MOVE_LEFT, KeyCode.A);
        bindings.put(Action.MOVE_RIGHT, KeyCode.D);
        bindings.put(Action.MOVE_DOWN, KeyCode.S);
        bindings.put(Action.ROTATE, KeyCode.W);
        bindings.put(Action.FAST_DROP, KeyCode.SPACE);
        bindings.put(Action.PAUSE, KeyCode.ESCAPE);
    }

    public KeyCode getBinding(Action action) {
        return bindings.get(action);
    }

    public void setBinding(Action action, KeyCode keyCode) {
        bindings.put(action, keyCode);
    }

    public Map<Action, KeyCode> getAllBindings() {
        return new HashMap<>(bindings);
    }

    public void resetToDefaults() {
        loadDefaults();
    }

    /**
     * Load bindings from a map (for persistence).
     */
    public void loadBindings(Map<Action, KeyCode> loadedBindings) {
        if (loadedBindings != null) {
            bindings.putAll(loadedBindings);
        }
    }
}
