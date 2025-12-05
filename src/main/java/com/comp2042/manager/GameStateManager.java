package com.comp2042.manager;

import com.comp2042.config.GameConfig;
import com.comp2042.controller.GuiController;
import com.comp2042.input.InputEventListener;
import com.comp2042.input.MoveEvent;
import com.comp2042.model.data.DownData;
import com.comp2042.view.panel.GameOverPanel;
import com.comp2042.view.renderer.BoardRenderer;
import com.comp2042.view.renderer.BrickRenderer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Manages game state including pause, game over, and game timeline.
 * Responsible for controlling game flow and state transitions.
 */
public class GameStateManager {
    private static final int GAME_TICK_DURATION_MS = GameConfig.GAME_TICK_DURATION_MS;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();
    private Timeline timeLine;
    private GridPane gamePanel;
    private GameOverPanel gameOverPanel;
    private InputEventListener eventListener;
    private NotificationManager notificationManager;
    private BrickRenderer brickRenderer;
    private BoardRenderer boardRenderer;
    private int[][] currentBoardMatrix;
    private GuiController guiController;

    /**
     * Sets the game over panel.
     * 
     * @param gameOverPanel The panel to display when the game ends.
     */
    public void setGameOverPanel(GameOverPanel gameOverPanel) {
        this.gameOverPanel = gameOverPanel;
    }

    /**
     * Sets the input event listener.
     * 
     * @param eventListener The listener for game events.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Sets the notification manager.
     * 
     * @param notificationManager The manager for displaying notifications.
     */
    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    /**
     * Sets the brick renderer.
     * 
     * @param brickRenderer The renderer for the active brick.
     */
    public void setBrickRenderer(BrickRenderer brickRenderer) {
        this.brickRenderer = brickRenderer;
    }

    /**
     * Sets the board renderer.
     * 
     * @param boardRenderer The renderer for the game board.
     */
    public void setBoardRenderer(BoardRenderer boardRenderer) {
        this.boardRenderer = boardRenderer;
    }

    /**
     * Sets the GUI controller.
     * 
     * @param guiController The GUI controller.
     */
    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }

    /**
     * Updates the current board matrix.
     * 
     * @param boardMatrix The new state of the board.
     */
    public void updateBoardMatrix(int[][] boardMatrix) {
        this.currentBoardMatrix = boardMatrix;
    }

    /**
     * Sets up the game timeline (game loop).
     *
     * @param gamePanel The main game panel.
     * @param onTick    The runnable to execute on each game tick.
     */
    public void setupTimeline(GridPane gamePanel, Runnable onTick) {
        this.gamePanel = gamePanel;
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(GAME_TICK_DURATION_MS),
                ae -> onTick.run()));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Handles the move down logic.
     * Checks if the game is paused before processing the move.
     * 
     * @param event The move event.
     */
    public void moveDown(MoveEvent event) {
        if (!isPause.get()) {
            DownData downData = eventListener.onDownEvent(event);
            handleDownData(downData);
        }
        gamePanel.requestFocus();
    }

    /**
     * Handles a fast drop action.
     * 
     * @param downData The result of the fast drop.
     */
    public void handleFastDrop(DownData downData) {
        handleDownData(downData);
    }

    private void handleDownData(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            notificationManager.showScoreNotification(downData.getClearRow().getScoreBonus());
        }

        // Refresh with ghost if we have board matrix
        if (currentBoardMatrix != null) {
            boardRenderer.refresh(currentBoardMatrix);
            brickRenderer.refreshWithGhost(downData.getViewData(), currentBoardMatrix);
        } else {
            brickRenderer.refresh(downData.getViewData());
        }
    }

    /**
     * Triggers the game over state.
     * Stops the timeline and shows the game over panel.
     */
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
    }

    /**
     * Starts a new game.
     * Resets the game state, hides the game over panel, and restarts the timeline.
     */
    public void newGame() {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        // Hide resume panel when starting new game
        if (guiController != null) {
            guiController.hideResumePanel();
        }
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.set(false);
        isGameOver.set(false);
    }

    /**
     * Toggles the pause state of the game.
     * Stops or plays the timeline accordingly.
     */
    public void togglePause() {
        if (isPause.get()) {
            // Resuming from pause - hide resume panel
            if (guiController != null) {
                guiController.hideResumePanel();
            }
            timeLine.play();
            isPause.set(false);
        } else {
            // Pausing game - show resume panel
            if (guiController != null) {
                guiController.showResumePanel();
            }
            timeLine.stop();
            isPause.set(true);
        }
        gamePanel.requestFocus();
    }

    /**
     * Checks if the game is currently paused.
     * 
     * @return true if paused, false otherwise.
     */
    public boolean isPaused() {
        return isPause.get();
    }

    /**
     * Checks if the game is over.
     * 
     * @return true if game over, false otherwise.
     */
    public boolean isGameOver() {
        return isGameOver.get();
    }
}
