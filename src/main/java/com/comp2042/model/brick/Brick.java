package com.comp2042.model.brick;

import java.util.List;

/**
 * Interface representing a Tetris brick that defines the contract for
 * retrieving the shape matrix of a brick.
 */
public interface Brick {

    /**
     * Gets the shape matrix of the brick.
     * 
     * @return A list of integer arrays representing the brick's shape and rotation
     *         states.
     */
    List<int[][]> getShapeMatrix();
}
