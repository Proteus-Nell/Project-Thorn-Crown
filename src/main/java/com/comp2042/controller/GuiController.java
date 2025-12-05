package com.comp2042.controller;

import com.comp2042.app.Main;
import com.comp2042.config.GameConfig;
import com.comp2042.input.InputEventListener;
import com.comp2042.input.KeyboardInputHandler;
import com.comp2042.manager.GameStateManager;
import com.comp2042.manager.NotificationManager;
import com.comp2042.model.data.ViewData;
import com.comp2042.view.panel.GameOverPanel;
import com.comp2042.view.panel.ResumePanel;
import com.comp2042.view.renderer.BoardRenderer;
import com.comp2042.view.renderer.BrickRenderer;
import com.comp2042.view.renderer.NextBrickRenderer;
import com.comp2042.input.EventSource;
import com.comp2042.input.EventType;
import com.comp2042.input.MoveEvent;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main GUI controller that coordinates all UI components.
 * Delegates responsibilities to specialized component classes.
 */
public class GuiController implements Initializable {

    @FXML
    private GridPane gamePanel;
    @FXML
    private Group groupNotification;
    @FXML
    private GridPane brickPanel;
    @FXML
    private GameOverPanel gameOverPanel;
    @FXML
    private ResumePanel resumePanel;
    @FXML
    private javafx.scene.control.Label scoreLabel;
    @FXML
    private GridPane nextBrickPanel;

    // Component delegates
    private KeyboardInputHandler keyboardHandler;
    private BoardRenderer boardRenderer;
    private BrickRenderer brickRenderer;
    private NextBrickRenderer nextBrickRenderer;
    private GameStateManager stateManager;
    private NotificationManager notificationManager;

    /**
     * Initializes the controller class.
     * Sets up the game components, loads resources, and wires dependencies.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load custom font
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(),
                GameConfig.DIGITAL_FONT_SIZE);

        // Initialize all component delegates
        boardRenderer = new BoardRenderer();
        brickRenderer = new BrickRenderer();
        nextBrickRenderer = new NextBrickRenderer();
        stateManager = new GameStateManager();
        notificationManager = new NotificationManager(groupNotification);
        keyboardHandler = new KeyboardInputHandler();

        // Wire up dependencies
        stateManager.setGameOverPanel(gameOverPanel);
        stateManager.setNotificationManager(notificationManager);
        stateManager.setBrickRenderer(brickRenderer);
        stateManager.setBoardRenderer(boardRenderer);
        stateManager.setGuiController(this);

        keyboardHandler.setGameStateManager(stateManager);
        keyboardHandler.setBrickRenderer(brickRenderer);
        keyboardHandler.setBoardRenderer(boardRenderer);

        gameOverPanel.setVisible(false);
        resumePanel.setVisible(false);
    }

    /**
     * Initializes the game view with initial board and brick data.
     * 
     * @param boardMatrix Initial board matrix
     * @param brick       Initial brick data
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        // Initialize renderers
        boardRenderer.initialize(boardMatrix, gamePanel);
        brickRenderer.initialize(brick, brickPanel, gamePanel, boardRenderer);
        nextBrickRenderer.initialize(brick.getNextBrickData(), nextBrickPanel);

        // Setup game timeline
        stateManager.setupTimeline(gamePanel,
                () -> stateManager.moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));

        // Setup keyboard controls
        keyboardHandler.setupKeyboardControls(gamePanel);

        // Initial render with ghost
        boardRenderer.refresh(boardMatrix);
        brickRenderer.refreshWithGhost(brick, boardMatrix);

        // Update keyboard handler and state manager with board matrix
        keyboardHandler.updateBoardMatrix(boardMatrix);
        stateManager.updateBoardMatrix(boardMatrix);
    }

    /**
     * Refreshes the game background (locked pieces).
     * 
     * @param board Current board matrix
     */
    public void refreshGameBackground(int[][] board) {
        // Update keyboard handler and state manager with latest board state
        keyboardHandler.updateBoardMatrix(board);
        stateManager.updateBoardMatrix(board);

        boardRenderer.refresh(board);

        // Render ghost using cached ViewData
        ViewData currentViewData = brickRenderer.getCurrentViewData();
        if (currentViewData != null) {
            brickRenderer.renderGhost(currentViewData, board);
            nextBrickRenderer.refresh(currentViewData.getNextBrickData());
        }
    }

    /**
     * Refreshes the game background with explicit ViewData.
     * Use this when ViewData has just been updated (e.g., after creating a new
     * brick).
     * 
     * @param board    Current board matrix
     * @param viewData Fresh ViewData from the board
     */
    public void refreshGameBackgroundWithViewData(int[][] board, ViewData viewData) {
        // Update keyboard handler and state manager with latest board state
        keyboardHandler.updateBoardMatrix(board);
        stateManager.updateBoardMatrix(board);

        boardRenderer.refresh(board);
        brickRenderer.renderGhost(viewData, board);
        // Use fresh ViewData to get accurate next brick
        nextBrickRenderer.refresh(viewData.getNextBrickData());
    }

    /**
     * Sets the event listener for game events.
     * 
     * @param eventListener The input event listener
     */
    public void setEventListener(InputEventListener eventListener) {
        keyboardHandler.setEventListener(eventListener);
        stateManager.setEventListener(eventListener);
    }

    /**
     * Triggers game over state.
     */
    public void gameOver() {
        stateManager.gameOver();
    }

    /**
     * Shows a score notification.
     * 
     * @param scoreBonus The score bonus to display
     */
    public void showScoreNotification(int scoreBonus) {
        notificationManager.showScoreNotification(scoreBonus);
    }

    /**
     * Binds the score label to a score property.
     * 
     * @param integerProperty The score property
     */
    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString());
    }

    // FXML event handlers

    /**
     * Handles new game button click.
     * 
     * @param actionEvent The action event
     */
    @FXML
    public void newGame(ActionEvent actionEvent) {
        stateManager.newGame();
    }

    /**
     * Handles pause button click.
     * 
     * @param actionEvent The action event
     */
    @FXML
    public void pauseGame(ActionEvent actionEvent) {
        // Don't allow pausing if game is over
        if (!stateManager.isGameOver()) {
            stateManager.togglePause();
        }
    }

    /**
     * Handles back to menu button click.
     * Pauses the game and switches back to the main menu.
     * Game can be resumed later.
     * 
     * @param actionEvent The action event
     */
    @FXML
    public void backToMenu(ActionEvent actionEvent) {
        resumePanel.setVisible(false); // Hide resume panel when going to menu
        // Only pause if not already paused (to avoid unpausing a paused game)
        if (!stateManager.isPaused()) {
            stateManager.togglePause();
        }
        Main.showMenu();
    }

    /**
     * Shows the resume panel instructing player to press ESC.
     */
    public void showResumePanel() {
        resumePanel.setVisible(true);
        gamePanel.requestFocus(); // Ensure keyboard input is captured
    }

    /**
     * Hides the resume panel and resumes gameplay.
     */
    public void hideResumePanel() {
        resumePanel.setVisible(false);
    }

    /**
     * Gets the game panel for scene restoration.
     * 
     * @return The game panel GridPane
     */
    public GridPane getGamePanel() {
        return gamePanel;
    }
}
