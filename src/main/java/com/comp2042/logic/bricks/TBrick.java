package com.comp2042.logic.bricks;

import com.comp2042.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'T' shaped Tetris brick.
 * This brick is T-shaped.
 */
final class TBrick implements Brick {

        private final List<int[][]> brickMatrix = new ArrayList<>();

        /**
         * Constructs a new TBrick.
         * Initializes the shape matrix with all the possible rotation states for the
         * T-brick.
         */
        public TBrick() {
                brickMatrix.add(new int[][] {
                                { 0, 0, 0, 0 },
                                { 6, 6, 6, 0 },
                                { 0, 6, 0, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 6, 0, 0 },
                                { 0, 6, 6, 0 },
                                { 0, 6, 0, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 6, 0, 0 },
                                { 6, 6, 6, 0 },
                                { 0, 0, 0, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 6, 0, 0 },
                                { 6, 6, 0, 0 },
                                { 0, 6, 0, 0 },
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
