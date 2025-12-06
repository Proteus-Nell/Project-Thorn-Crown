package com.comp2042.controller;

import com.comp2042.app.Main;
import com.comp2042.manager.SettingsManager;
import com.comp2042.model.settings.DifficultyMode;
import com.comp2042.model.settings.KeyBindings;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;

/**
 * Controller for the Settings screen.
 * Handles music, difficulty, and keybinding configuration.
 */
public class SettingsController {

    @FXML
    private CheckBox musicMuteCheckbox;
    @FXML
    private ComboBox<DifficultyMode> difficultyComboBox;

    // Keybind buttons
    @FXML
    private Button leftKeyButton;
    @FXML
    private Button rightKeyButton;
    @FXML
    private Button downKeyButton;
    @FXML
    private Button rotateKeyButton;
    @FXML
    private Button dropKeyButton;
    @FXML
    private Button pauseKeyButton;
    @FXML
    private Button resetKeysButton;

    private SettingsManager settings;
    private Button currentlyRebindingButton = null;
    private KeyBindings.Action currentlyRebindingAction = null;

    @FXML
    public void initialize() {
        settings = SettingsManager.getInstance();

        // Load stylesheet after scene is ready
        Platform.runLater(() -> {
            try {
                String css = getClass().getResource("/window_style.css").toExternalForm();
                leftKeyButton.getScene().getStylesheets().add(css);
            } catch (Exception e) {
                // Stylesheet not critical
            }
        });

        // Bind music mute checkbox
        musicMuteCheckbox.selectedProperty().bindBidirectional(settings.musicMutedProperty());

        // Setup difficulty dropdown
        difficultyComboBox.getItems().addAll(DifficultyMode.values());
        difficultyComboBox.setValue(settings.getDifficulty());
        difficultyComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                settings.setDifficulty(newVal);
            }
        });

        // Setup keybind buttons
        setupKeybindButton(leftKeyButton, KeyBindings.Action.MOVE_LEFT);
        setupKeybindButton(rightKeyButton, KeyBindings.Action.MOVE_RIGHT);
        setupKeybindButton(downKeyButton, KeyBindings.Action.MOVE_DOWN);
        setupKeybindButton(rotateKeyButton, KeyBindings.Action.ROTATE);
        setupKeybindButton(dropKeyButton, KeyBindings.Action.FAST_DROP);
        setupKeybindButton(pauseKeyButton, KeyBindings.Action.PAUSE);

        updateAllKeybindButtons();
    }

    private void setupKeybindButton(Button button, KeyBindings.Action action) {
        button.setOnAction(e -> startRebinding(button, action));
        button.setOnKeyPressed(this::handleKeyPressed);
    }

    private void startRebinding(Button button, KeyBindings.Action action) {
        if (currentlyRebindingButton != null) {
            // Cancel previous rebinding
            currentlyRebindingButton.setStyle("");
        }

        currentlyRebindingButton = button;
        currentlyRebindingAction = action;
        button.setText("Press key...");
        button.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white;");
        button.requestFocus();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (currentlyRebindingButton != null && currentlyRebindingAction != null) {
            // Set the new keybinding
            settings.getKeyBindings().setBinding(currentlyRebindingAction, event.getCode());
            settings.saveSettings();

            // Update UI
            currentlyRebindingButton.setStyle("");
            updateAllKeybindButtons();

            currentlyRebindingButton = null;
            currentlyRebindingAction = null;
            event.consume();
        }
    }

    private void updateAllKeybindButtons() {
        KeyBindings bindings = settings.getKeyBindings();
        leftKeyButton.setText(bindings.getBinding(KeyBindings.Action.MOVE_LEFT).toString());
        rightKeyButton.setText(bindings.getBinding(KeyBindings.Action.MOVE_RIGHT).toString());
        downKeyButton.setText(bindings.getBinding(KeyBindings.Action.MOVE_DOWN).toString());
        rotateKeyButton.setText(bindings.getBinding(KeyBindings.Action.ROTATE).toString());
        dropKeyButton.setText(bindings.getBinding(KeyBindings.Action.FAST_DROP).toString());
        pauseKeyButton.setText(bindings.getBinding(KeyBindings.Action.PAUSE).toString());
    }

    @FXML
    void onResetKeys(ActionEvent event) {
        settings.getKeyBindings().resetToDefaults();
        settings.saveSettings();
        updateAllKeybindButtons();
    }

    @FXML
    void onBackToMenu(ActionEvent event) {
        Main.showMainMenu();
    }
}
