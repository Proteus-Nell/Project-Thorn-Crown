package com.comp2042.model.brick;

/**
 * Interface for generating Tetris bricks.
 * Defines methods to get the current and next brick in the sequence.
 */
public interface BrickGenerator {

    /**
     * Retrieves the current brick to be played.
     *
     * @return The current {@link Brick}.
     */
    Brick getBrick();

    /**
     * Peeks at the next brick that will be generated.
     *
     * @return The next {@link Brick}.
     */
    Brick getNextBrick();
}
