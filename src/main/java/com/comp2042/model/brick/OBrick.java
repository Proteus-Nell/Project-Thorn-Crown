package com.comp2042.model.brick;

import com.comp2042.model.board.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'O' shaped Tetris brick.
 * This brick is a square block.
 */
final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a new OBrick.
     * Initializes the shape matrix with all the possible rotation states for the
     * O-brick.
     */
    public OBrick() {
        brickMatrix.add(new int[][] {
                { 0, 0, 0, 0 },
                { 0, 4, 4, 0 },
                { 0, 4, 4, 0 },
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
