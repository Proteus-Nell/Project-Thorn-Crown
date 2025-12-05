package com.comp2042.controller;

import com.comp2042.config.GameConfig;
import com.comp2042.input.InputEventListener;
import com.comp2042.input.MoveEvent;
import com.comp2042.model.board.Board;
import com.comp2042.model.board.SimpleBoard;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.ViewData;
import com.comp2042.model.score.ClearRow;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Controls the main game logic and flow.
 * Handles user input events, updates the game state, and manages the game loop.
 */
public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(GameConfig.BOARD_WIDTH, GameConfig.BOARD_HEIGHT);
    private final GuiController viewGuiController;

    // Lock delay timer components
    private Timeline lockTimer;
    private boolean lockDelayActive = false;
    private int lockResetCount = 0;

    /**
     * Constructs a new GameController.
     * Initializes the board and binds the GUI controller.
     * 
     * @param c The {@link GuiController} responsible for the view.
     */
    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    /**
     * Handles the 'down' event (moving the brick down).
     * Manages lock delay logic when the brick cannot move further.
     *
     * @param event The move event.
     * @return The {@link DownData} containing the result of the move.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();

        if (canMove) {
            // Piece moved down successfully
            stopLockDelay();
            return new DownData(null, board.getViewData());
        }

        // Piece cannot move down - start/wait for lock delay
        if (GameConfig.LOCK_DELAY_ENABLED && !lockDelayActive) {
            startLockDelay();
        } else if (!GameConfig.LOCK_DELAY_ENABLED) {
            return lockPieceAndReturn();
        }

        return new DownData(null, board.getViewData());
    }

    /**
     * Handles the 'left' event (moving the brick left).
     * Resets lock delay if active and move is successful.
     *
     * @param event The move event.
     * @return The updated {@link ViewData}.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        boolean moved = board.moveBrickLeft();
        if (moved && lockDelayActive) {
            resetLockDelay();
        }
        return board.getViewData();
    }

    /**
     * Handles the 'right' event (moving the brick right).
     * Resets lock delay if active and move is successful.
     *
     * @param event The move event.
     * @return The updated {@link ViewData}.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        boolean moved = board.moveBrickRight();
        if (moved && lockDelayActive) {
            resetLockDelay();
        }
        return board.getViewData();
    }

    /**
     * Handles the 'rotate' event (rotating the brick).
     * Resets lock delay if active and rotation is successful.
     *
     * @param event The move event.
     * @return The updated {@link ViewData}.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        boolean rotated = board.rotateLeftBrick();
        if (rotated && lockDelayActive) {
            resetLockDelay();
        }
        return board.getViewData();
    }

    /**
     * Handles the 'fast drop' event (dropping the brick to the bottom).
     * Locks the piece immediately.
     *
     * @param event The move event.
     * @return The {@link DownData} containing the result of the drop.
     */
    @Override
    public DownData onFastDropEvent(MoveEvent event) {
        stopLockDelay();

        // Drop piece to bottom
        while (board.moveBrickDown()) {
        }

        // Lock immediately (no delay for fast drop)
        return lockPieceAndReturn();
    }

    /**
     * Starts a new game.
     * Resets the board and game state.
     */
    @Override
    public void createNewGame() {
        stopLockDelay();
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Starts the lock delay timer.
     */
    private void startLockDelay() {
        lockDelayActive = true;
        lockResetCount = 0;
        createLockTimer();
    }

    /**
     * Resets the lock delay timer if the maximum resets haven't been reached.
     */
    private void resetLockDelay() {
        lockResetCount++;

        // Max resets reached, lock tetronimo.
        if (lockResetCount >= GameConfig.MAX_LOCK_RESETS) {
            lockPieceNow();
        } else {
            // Reset timer
            createLockTimer();
        }
    }

    /**
     * Stops the lock delay timer.
     */
    private void stopLockDelay() {
        if (lockTimer != null) {
            lockTimer.stop();
            lockTimer = null;
        }
        lockDelayActive = false;
        lockResetCount = 0;
    }

    /**
     * Creates and starts the timeline for lock delay.
     */
    private void createLockTimer() {
        if (lockTimer != null) {
            lockTimer.stop();
        }

        lockTimer = new Timeline(new KeyFrame(
                Duration.millis(GameConfig.LOCK_DELAY_MS),
                event -> lockPieceNow()));
        lockTimer.setCycleCount(1);
        lockTimer.play();
    }

    // Locks piece and updates GUI (called by timer).
    /**
     * Locks the piece immediately.
     * Called when the lock timer expires.
     */
    private void lockPieceNow() {
        // Verify the piece still cannot move down before locking
        // This prevents race conditions where the piece is moved just as the timer
        // expires
        if (((SimpleBoard) board).canMoveDown()) {
            // Piece can still move down, don't lock it
            stopLockDelay();
            return;
        }

        stopLockDelay();
        board.mergeBrickToBackground();

        // Check for cleared rows and show notification
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            viewGuiController.showScoreNotification(clearRow.getScoreBonus());
        }

        // Create new piece
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        // Update display with fresh ViewData
        ViewData newViewData = board.getViewData();
        viewGuiController.refreshGameBackgroundWithViewData(board.getBoardMatrix(), newViewData);
    }

    // Locks piece and returns DownData (for immediate locking scenarios).
    /**
     * Locks the piece and returns the result.
     * Used for fast drops or when lock delay is disabled.
     *
     * @return The {@link DownData} result.
     */
    private DownData lockPieceAndReturn() {
        stopLockDelay();
        board.mergeBrickToBackground();

        // Check for cleared rows
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
        }

        // Create new piece
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        // Update display with fresh ViewData
        ViewData newViewData = board.getViewData();
        viewGuiController.refreshGameBackgroundWithViewData(board.getBoardMatrix(), newViewData);

        return new DownData(clearRow, newViewData);
    }
}
