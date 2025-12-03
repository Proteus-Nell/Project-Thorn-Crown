package com.comp2042;

/**
 * Interface representing the game board logic.
 * Defines the core actions that can be performed on the board.
 */
public interface Board {

    /**
     * Moves the current brick down by one row.
     * 
     * @return TRUE if the move was successful, FALSE otherwise.
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick to the left.
     * 
     * @return TRUE if the move was successful, FALSE otherwise.
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick to the right
     * 
     * @return TRUE if the move was successful, FALSE otherwise.
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick to the left (counter-clockwise).
     * 
     * @return TRUE if the rotation was successful, FALSE otherwise.
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick at the top of the board.
     * 
     * @return TRUE if the brick was created successfully, FALSE if the game is over
     *         (no space).
     */
    boolean createNewBrick();

    /**
     * Gets the current state of the board matrix.
     * 
     * @return A 2D array representing the board.
     */
    int[][] getBoardMatrix();

    /**
     * Gets the view data for the current brick.
     * 
     * @return The {@link ViewData} object containing brick position and shape.
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the background board matrix.
     * This locks the brick in place.
     */
    void mergeBrickToBackground();

    /**
     * Clears any full rows on the board.
     *
     * @return A {@link ClearRow} object containing information about cleared rows
     *         and score.
     */
    ClearRow clearRows();

    /**
     * Gets the current score object.
     * 
     * @return The {@link Score} object.
     */
    Score getScore();

    /**
     * Resets the board for a new game.
     */
    void newGame();
}
