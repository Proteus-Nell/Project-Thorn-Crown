package com.comp2042.model.brick;

import com.comp2042.model.board.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'S' shaped Tetris brick.
 * This brick is S-shaped.
 */
final class SBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a new SBrick.
     * Initializes the shape matrix with all the possible rotation states for the
     * S-brick.
     */
    public SBrick() {
        brickMatrix.add(new int[][] {
                { 0, 0, 0, 0 },
                { 0, 5, 5, 0 },
                { 5, 5, 0, 0 },
                { 0, 0, 0, 0 }
        });
        brickMatrix.add(new int[][] {
                { 5, 0, 0, 0 },
                { 5, 5, 0, 0 },
                { 0, 5, 0, 0 },
                { 0, 0, 0, 0 }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
