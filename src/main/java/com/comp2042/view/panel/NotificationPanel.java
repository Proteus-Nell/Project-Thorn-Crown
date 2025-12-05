package com.comp2042.view.panel;

import com.comp2042.config.GameConfig;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Panel for displaying temporary notifications (like score bonuses).
 * Handles the visual style and animation of notifications.
 */
public class NotificationPanel extends BorderPane {

    /**
     * Constructs a new NotificationPanel.
     *
     * @param text The text to display (e.g., score bonus).
     */
    public NotificationPanel(String text) {
        setMinHeight(GameConfig.NOTIFICATION_PANEL_MIN_HEIGHT);
        setMinWidth(GameConfig.NOTIFICATION_PANEL_MIN_WIDTH);
        final Label score = new Label(text);
        score.getStyleClass().add("bonusStyle");
        final Effect glow = new Glow(GameConfig.NOTIFICATION_GLOW_INTENSITY);
        score.setEffect(glow);
        score.setTextFill(Color.WHITE);
        setCenter(score);

    }

    /**
     * Shows the notification with a fade and translate animation.
     * Removes itself from the parent list when animation completes.
     *
     * @param list The list of children nodes to remove this panel from.
     */
    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(GameConfig.NOTIFICATION_FADE_DURATION_MS), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(GameConfig.NOTIFICATION_TRANSLATE_DURATION_MS),
                this);
        tt.setToY(this.getLayoutY() - GameConfig.NOTIFICATION_MOVE_DISTANCE);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.remove(NotificationPanel.this);
            }
        });
        transition.play();
    }
}
