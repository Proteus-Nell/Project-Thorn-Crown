package com.comp2042.manager;

import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages audio playback for the game.
 * Handles playlist management, playback controls, and volume.
 */
public class AudioManager {

    private static AudioManager instance;

    private List<String> playlist;
    private int currentTrackIndex = 0;
    private MediaPlayer mediaPlayer;

    // Observable properties for UI binding
    private final StringProperty currentSongName = new SimpleStringProperty("No Song");
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final DoubleProperty currentTime = new SimpleDoubleProperty(0.0);
    private final DoubleProperty totalDuration = new SimpleDoubleProperty(0.0);
    private final DoubleProperty volume = new SimpleDoubleProperty(0.5);

    private AudioManager() {
        playlist = new ArrayList<>();
        loadMusicFiles();
        if (!playlist.isEmpty()) {
            loadTrack(0);
        }
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Loads all music files from the resources/music directory.
     */
    private void loadMusicFiles() {
        try {
            // Try to load from resources
            URL musicDirUrl = getClass().getResource("/music");
            if (musicDirUrl != null) {
                File musicDir = new File(musicDirUrl.toURI());
                if (musicDir.exists() && musicDir.isDirectory()) {
                    File[] files = musicDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3") ||
                            name.toLowerCase().endsWith(".wav"));

                    if (files != null) {
                        for (File file : files) {
                            playlist.add(file.toURI().toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading music files: " + e.getMessage());
        }

        // If no files found, add a placeholder
        if (playlist.isEmpty()) {
            System.out.println("No music files found in resources/music");
        }
    }

    /**
     * Loads a specific track from the playlist.
     */
    private void loadTrack(int index) {
        if (playlist.isEmpty())
            return;

        currentTrackIndex = index;
        boolean wasPlaying = isPlaying.get();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        try {
            Media media = new Media(playlist.get(currentTrackIndex));
            mediaPlayer = new MediaPlayer(media);

            // Set up listeners
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                currentTime.set(newTime.toSeconds());
            });

            mediaPlayer.setOnReady(() -> {
                totalDuration.set(mediaPlayer.getTotalDuration().toSeconds());
                updateSongName();
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                skipForward();
            });

            mediaPlayer.setVolume(volume.get());
            volume.addListener((obs, oldVol, newVol) -> {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(newVol.doubleValue());
                }
            });

            if (wasPlaying) {
                play();
            }

        } catch (Exception e) {
            System.err.println("Error loading track: " + e.getMessage());
        }
    }

    /**
     * Updates the current song name from the file path.
     */
    private void updateSongName() {
        if (playlist.isEmpty()) {
            currentSongName.set("No Song");
            return;
        }

        try {
            String path = playlist.get(currentTrackIndex);
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            // URL decode the filename (handles %20 for spaces, etc.)
            fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            // Remove file extension
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            }
            currentSongName.set(fileName);
        } catch (Exception e) {
            currentSongName.set("Unknown Song");
        }
    }

    public void play() {
        if (mediaPlayer != null && !playlist.isEmpty()) {
            mediaPlayer.play();
            isPlaying.set(true);
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying.set(false);
        }
    }

    public void togglePlayPause() {
        if (isPlaying.get()) {
            pause();
        } else {
            play();
        }
    }

    public void skipForward() {
        if (playlist.isEmpty())
            return;
        int nextIndex = (currentTrackIndex + 1) % playlist.size();
        loadTrack(nextIndex);
        if (isPlaying.get()) {
            play();
        }
    }

    public void skipBackward() {
        if (playlist.isEmpty())
            return;
        int prevIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
        loadTrack(prevIndex);
        if (isPlaying.get()) {
            play();
        }
    }

    public void seek(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    public void setVolume(double vol) {
        volume.set(Math.max(0.0, Math.min(1.0, vol)));
    }

    // Property getters for UI binding
    public StringProperty currentSongNameProperty() {
        return currentSongName;
    }

    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public DoubleProperty currentTimeProperty() {
        return currentTime;
    }

    public DoubleProperty totalDurationProperty() {
        return totalDuration;
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public String getCurrentSongName() {
        return currentSongName.get();
    }

    public boolean isPlaying() {
        return isPlaying.get();
    }
}
