package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(25, 10);
    private final GuiController viewGuiController;

    // Lock delay timer components
    private Timeline lockTimer;
    private boolean lockDelayActive = false;
    private int lockResetCount = 0;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();

        if (canMove) {
            // Piece moved down successfully
            stopLockDelay();
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
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

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        boolean moved = board.moveBrickLeft();
        if (moved && lockDelayActive) {
            resetLockDelay();
        }
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        boolean moved = board.moveBrickRight();
        if (moved && lockDelayActive) {
            resetLockDelay();
        }
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        boolean rotated = board.rotateLeftBrick();
        if (rotated && lockDelayActive) {
            resetLockDelay();
        }
        return board.getViewData();
    }

    @Override
    public DownData onFastDropEvent(MoveEvent event) {
        stopLockDelay();

        // Drop piece to bottom
        while (board.moveBrickDown()) {
        }

        // Lock immediately (no delay for fast drop)
        return lockPieceAndReturn();
    }

    @Override
    public void createNewGame() {
        stopLockDelay();
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    private void startLockDelay() {
        lockDelayActive = true;
        lockResetCount = 0;
        createLockTimer();
    }

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

    private void stopLockDelay() {
        if (lockTimer != null) {
            lockTimer.stop();
            lockTimer = null;
        }
        lockDelayActive = false;
        lockResetCount = 0;
    }

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
    private void lockPieceNow() {
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

        // Update display
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    // Locks piece and returns DownData (for immediate locking scenarios).
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

        // Update display
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }
}
