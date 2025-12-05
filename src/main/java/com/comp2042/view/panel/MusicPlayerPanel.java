package com.comp2042.view.panel;

import com.comp2042.manager.AudioManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

/**
 * Music player panel with playback controls, progress slider, and volume
 * control.
 * Displays in a rectangular container with song name, control buttons, and
 * sliders.
 */
public class MusicPlayerPanel extends VBox {

    private final AudioManager audioManager;

    private Label songNameLabel;
    private Button playPauseButton;
    private Button skipBackButton;
    private Button skipForwardButton;
    private Slider progressSlider;
    private Label currentTimeLabel;
    private Label totalTimeLabel;
    private Slider volumeSlider;

    private boolean userSeeking = false;

    public MusicPlayerPanel() {
        this.audioManager = AudioManager.getInstance();
        initializeUI();
        bindToAudioManager();
        setupStyles();
    }

    private void initializeUI() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(8);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #2c2c2c; -fx-background-radius: 8;");
        this.setPrefWidth(180);

        // Song name label at top
        songNameLabel = new Label("No Song");
        songNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
        songNameLabel.setMaxWidth(Double.MAX_VALUE);
        songNameLabel.setAlignment(Pos.CENTER);
        songNameLabel.setWrapText(true);

        // Control buttons (centered horizontally)
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);

        skipBackButton = new Button("⏮");
        playPauseButton = new Button("▶");
        skipForwardButton = new Button("⏭");

        styleButton(skipBackButton);
        styleButton(playPauseButton);
        styleButton(skipForwardButton);

        controlBox.getChildren().addAll(skipBackButton, playPauseButton, skipForwardButton);

        // Progress slider with timestamps
        HBox progressBox = new HBox(5);
        progressBox.setAlignment(Pos.CENTER);

        currentTimeLabel = new Label("0:00");
        totalTimeLabel = new Label("0:00");
        styleTimeLabel(currentTimeLabel);
        styleTimeLabel(totalTimeLabel);

        progressSlider = new Slider(0, 100, 0);
        progressSlider.setPrefWidth(120);
        progressSlider.setStyle("-fx-background-color: transparent;");

        progressBox.getChildren().addAll(currentTimeLabel, progressSlider, totalTimeLabel);

        // Volume control
        HBox volumeBox = new HBox(5);
        volumeBox.setAlignment(Pos.CENTER);

        Label volumeLabel = new Label("🔊");
        volumeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setPrefWidth(120);
        volumeSlider.setStyle("-fx-background-color: transparent;");

        volumeBox.getChildren().addAll(volumeLabel, volumeSlider);

        // Add all components
        this.getChildren().addAll(
                songNameLabel,
                new Region(), // Spacer
                controlBox,
                progressBox,
                volumeBox);

        setupEventHandlers();
    }

    private void styleButton(Button button) {
        button.setStyle(
                "-fx-background-color: #3c3c3c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 5 10; " +
                        "-fx-background-radius: 4; " +
                        "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #4c4c4c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 5 10; " +
                        "-fx-background-radius: 4; " +
                        "-fx-cursor: hand;"));

        button.setOnMouseExited(e -> styleButton(button));
    }

    private void styleTimeLabel(Label label) {
        label.setStyle("-fx-text-fill: #aaa; -fx-font-size: 10px;");
        label.setMinWidth(35);
    }

    private void setupEventHandlers() {
        // Play/Pause button
        playPauseButton.setOnAction(e -> audioManager.togglePlayPause());

        // Skip buttons
        skipBackButton.setOnAction(e -> audioManager.skipBackward());
        skipForwardButton.setOnAction(e -> audioManager.skipForward());

        // Progress slider - user seeking
        progressSlider.setOnMousePressed(e -> userSeeking = true);
        progressSlider.setOnMouseReleased(e -> {
            audioManager.seek(progressSlider.getValue());
            userSeeking = false;
        });

        // Volume slider
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setVolume(newVal.doubleValue());
        });
    }

    private void bindToAudioManager() {
        // Bind song name
        audioManager.currentSongNameProperty().addListener((obs, oldName, newName) -> {
            songNameLabel.setText(newName);
        });

        // Bind play/pause button text
        audioManager.isPlayingProperty().addListener((obs, oldVal, newVal) -> {
            playPauseButton.setText(newVal ? "⏸" : "▶");
        });

        // Bind progress slider
        audioManager.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!userSeeking) {
                progressSlider.setValue(newTime.doubleValue());
                currentTimeLabel.setText(formatTime(newTime.doubleValue()));
            }
        });

        audioManager.totalDurationProperty().addListener((obs, oldDuration, newDuration) -> {
            progressSlider.setMax(newDuration.doubleValue());
            totalTimeLabel.setText(formatTime(newDuration.doubleValue()));
        });

        // Bind volume slider
        volumeSlider.setValue(audioManager.volumeProperty().get());

        // Initialize with current values from AudioManager
        songNameLabel.setText(audioManager.getCurrentSongName());
        currentTimeLabel.setText(formatTime(audioManager.currentTimeProperty().get()));
        totalTimeLabel.setText(formatTime(audioManager.totalDurationProperty().get()));
        progressSlider.setMax(audioManager.totalDurationProperty().get());
        playPauseButton.setText(audioManager.isPlaying() ? "⏸" : "▶");
    }

    private void setupStyles() {
        // Additional styling can be added here or via CSS
    }

    /**
     * Formats seconds into M:SS format.
     */
    private String formatTime(double seconds) {
        int totalSec = (int) seconds;
        int minutes = totalSec / 60;
        int secs = totalSec % 60;
        return String.format("%d:%02d", minutes, secs);
    }
}
