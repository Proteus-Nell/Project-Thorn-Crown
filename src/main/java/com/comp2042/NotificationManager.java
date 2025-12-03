package com.comp2042;

import javafx.scene.Group;

/**
 * Manages score notification display and animations.
 * Responsible for showing score bonuses when rows are cleared.
 */
public class NotificationManager {
    private final Group groupNotification;

    /**
     * Constructs a new NotificationManager.
     * 
     * @param groupNotification The group container for notifications.
     */
    public NotificationManager(Group groupNotification) {
        this.groupNotification = groupNotification;
    }

    /**
     * Shows a score notification with animation.
     * 
     * @param scoreBonus The score bonus to display
     */
    public void showScoreNotification(int scoreBonus) {
        NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
    }
}
