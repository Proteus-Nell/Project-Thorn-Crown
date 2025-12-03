package com.comp2042;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * Handles all keyboard input and delegates to appropriate components.
 * Responsible for translating keyboard events into game actions.
 */
public class KeyboardInputHandler {
    private InputEventListener eventListener;
    private GameStateManager stateManager;
    private BrickRenderer brickRenderer;
    private BoardRenderer boardRenderer;
    private int[][] currentBoardMatrix;

    /**
     * Sets the input event listener.
     *
     * @param eventListener The listener for game events.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Sets the game state manager.
     *
     * @param stateManager The manager for game state.
     */
    public void setGameStateManager(GameStateManager stateManager) {
        this.stateManager = stateManager;
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
     * Updates the current board matrix.
     *
     * @param boardMatrix The new state of the board.
     */
    public void updateBoardMatrix(int[][] boardMatrix) {
        this.currentBoardMatrix = boardMatrix;
    }

    /**
     * Sets up keyboard controls for the game panel.
     * Requests focus and registers the key press handler.
     *
     * @param gamePanel The main game panel.
     */
    public void setupKeyboardControls(GridPane gamePanel) {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Handles key press events.
     * Delegates to specific handlers based on game state and key code.
     *
     * @param keyEvent The key event.
     */
    private void handleKeyPress(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        // Game controls (only when not paused/game over)
        if (!stateManager.isPaused() && !stateManager.isGameOver()) {
            boolean handled = handleGameControls(code);
            if (handled) {
                keyEvent.consume();
            }
        }

        // Universal controls (pause, new game)
        handleUniversalControls(code);
    }

    /**
     * Handles game control keys (movement, rotation).
     *
     * @param code The key code.
     * @return true if the key was handled, false otherwise.
     */
    private boolean handleGameControls(KeyCode code) {
        return switch (code) {
            case LEFT, A -> {
                ViewData viewData = eventListener.onLeftEvent(
                        new MoveEvent(EventType.LEFT, EventSource.USER));
                refreshBrickWithGhost(viewData);
                yield true;
            }
            case RIGHT, D -> {
                ViewData viewData = eventListener.onRightEvent(
                        new MoveEvent(EventType.RIGHT, EventSource.USER));
                refreshBrickWithGhost(viewData);
                yield true;
            }
            case UP, W -> {
                ViewData viewData = eventListener.onRotateEvent(
                        new MoveEvent(EventType.ROTATE, EventSource.USER));
                refreshBrickWithGhost(viewData);
                yield true;
            }
            case DOWN, S -> {
                stateManager.moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                yield true;
            }
            case SPACE -> {
                stateManager.handleFastDrop(eventListener.onFastDropEvent(
                        new MoveEvent(EventType.FAST_DROP, EventSource.USER)));
                yield true;
            }
            default -> false;
        };
    }

    /**
     * Refreshes the brick display, including the ghost preview.
     *
     * @param viewData The current view data of the brick.
     */
    private void refreshBrickWithGhost(ViewData viewData) {
        if (currentBoardMatrix != null) {
            // First refresh the board to clear old ghost
            boardRenderer.refresh(currentBoardMatrix);
            // Then refresh brick with new ghost
            brickRenderer.refreshWithGhost(viewData, currentBoardMatrix);
        } else {
            // Fallback if board matrix not available
            brickRenderer.refresh(viewData);
        }
    }

    /**
     * Handles universal control keys (pause, new game).
     * 
     * @param code The key code.
     */
    private void handleUniversalControls(KeyCode code) {
        switch (code) {
            case N -> stateManager.newGame();
            case P, ESCAPE -> stateManager.togglePause();
            default -> {
            }
        }
    }
}
