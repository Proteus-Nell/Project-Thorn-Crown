package com.comp2042;

/**
 * Interface for handling game input events.
 * Defines the contract for processing user actions like moving, rotating, and
 * dropping bricks.
 */
public interface InputEventListener {

    /**
     * Handles a 'down' movement event.
     *
     * @param event The move event.
     * @return The result of the downward movement.
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles a 'left' movement event.
     *
     * @param event The move event.
     * @return The updated view data after the move.
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles a 'right' movement event.
     *
     * @param event The move event.
     * @return The updated view data after the move.
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles a 'rotate' movement event.
     *
     * @param event The move event.
     * @return The updated view data after the rotation.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles a 'fast drop' event.
     *
     * @param event The move event.
     * @return The result of the fast drop.
     */
    DownData onFastDropEvent(MoveEvent event);

    /**
     * Initiates a new game.
     */
    void createNewGame();
}
