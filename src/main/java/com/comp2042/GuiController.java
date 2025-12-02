package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    // Color mapping for brick types
    private static final Color[] BRICK_COLORS = {
            Color.TRANSPARENT, // 0
            Color.AQUA, // 1
            Color.BLUEVIOLET, // 2
            Color.DARKGREEN, // 3
            Color.YELLOW, // 4
            Color.RED, // 5
            Color.BEIGE, // 6
            Color.BURLYWOOD // 7
    };

    // Constants
    private static final int BRICK_SIZE = 20;
    private static final int BOARD_ROW_OFFSET = 2; // Skip top 2 rows for spawn area
    private static final int BRICK_PANEL_Y_OFFSET = -42; // Vertical alignment offset
    private static final int GAME_TICK_DURATION_MS = 400; // Auto-drop interval

    @FXML
    private GridPane gamePanel;
    @FXML
    private Group groupNotification;
    @FXML
    private GridPane brickPanel;
    @FXML
    private GameOverPanel gameOverPanel;
    @FXML
    private javafx.scene.control.Label scoreLabel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        setupKeyboardControls();
        gameOverPanel.setVisible(false);
        setupVisualEffects();
    }

    private void setupKeyboardControls() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        // Game controls
        if (!isPause.get() && !isGameOver.get()) {
            boolean handled = switch (code) {
                case LEFT, A -> {
                    refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                    yield true;
                }
                case RIGHT, D -> {
                    refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    yield true;
                }
                case UP, W -> {
                    refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                    yield true;
                }
                case DOWN, S -> {
                    moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    yield true;
                }
                case SPACE -> {
                    handleDownData(eventListener.onFastDropEvent(new MoveEvent(EventType.FAST_DROP, EventSource.USER)));
                    yield true;
                }
                default -> false;

            };
            if (handled) {
                keyEvent.consume();
            }
        }

        // Universal controls
        switch (code) {
            case N -> newGame(null);
            case P, ESCAPE -> pauseGame(null);
            default -> {
            }
        }
    }

    private void setupVisualEffects() {
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
        // Not in use right now
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        initializeDisplayMatrix(boardMatrix);
        initializeBrickPanel(brick);
        setupGameTimeline();
    }

    public void initializeDisplayMatrix(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = BOARD_ROW_OFFSET; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - BOARD_ROW_OFFSET);
            }
        }
    }

    public void initializeBrickPanel(ViewData brick) {
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        calculateBrickPanelLayout(brick);
    }

    public void setupGameTimeline() {
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(GAME_TICK_DURATION_MS),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int colorIndex) {
        return (colorIndex >= 0 && colorIndex < BRICK_COLORS.length) ? BRICK_COLORS[colorIndex] : Color.WHITE;
    }

    private void calculateBrickPanelLayout(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap()
                + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(BRICK_PANEL_Y_OFFSET + gamePanel.getLayoutY()
                + brick.getyPosition() * brickPanel.getHgap()
                + brick.getyPosition() * BRICK_SIZE);
    }

    private void refreshBrick(ViewData brick) {
        if (!isPause.get()) {
            calculateBrickPanelLayout(brick);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = BOARD_ROW_OFFSET; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (!isPause.get()) {
            handleDownData(eventListener.onDownEvent(event));
        }
        gamePanel.requestFocus();
    }

    private void handleDownData(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel(
                    "+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
        refreshBrick(downData.getViewData());
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.set(false);
        isGameOver.set(false);
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (isPause.get()) {
            timeLine.play();
            isPause.set(false);
        } else {
            timeLine.stop();
            isPause.set(true);
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void showScoreNotification(int scoreBonus) {
        NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString());
    }
}
