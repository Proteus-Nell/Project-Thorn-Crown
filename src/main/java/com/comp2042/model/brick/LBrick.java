package com.comp2042.model.brick;

import com.comp2042.model.board.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'L' shaped Tetris brick.
 * This brick is L-shaped.
 */
final class LBrick implements Brick {

        private final List<int[][]> brickMatrix = new ArrayList<>();

        /**
         * Constructs a new LBrick.
         * Initializes the shape matrix with all the possible rotation states for the
         * L-brick.
         */
        public LBrick() {
                brickMatrix.add(new int[][] {
                                { 0, 0, 0, 0 },
                                { 0, 3, 3, 3 },
                                { 0, 3, 0, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 0, 0, 0 },
                                { 0, 3, 3, 0 },
                                { 0, 0, 3, 0 },
                                { 0, 0, 3, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 0, 0, 0 },
                                { 0, 0, 3, 0 },
                                { 3, 3, 3, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 3, 0, 0 },
                                { 0, 3, 0, 0 },
                                { 0, 3, 3, 0 },
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
