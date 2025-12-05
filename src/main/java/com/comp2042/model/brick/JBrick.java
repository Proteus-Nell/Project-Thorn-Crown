package com.comp2042.model.brick;

import com.comp2042.model.board.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'J' shaped Tetris brick.
 * This brick is L-shaped but mirrored.
 */
final class JBrick implements Brick {

        private final List<int[][]> brickMatrix = new ArrayList<>();

        /**
         * Constructs a new JBrick.
         * Initializes the shape matrix with all the possible rotation states for the
         * J-brick.
         */
        public JBrick() {
                brickMatrix.add(new int[][] {
                                { 0, 0, 0, 0 },
                                { 2, 2, 2, 0 },
                                { 0, 0, 2, 0 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 0, 0, 0 },
                                { 0, 2, 2, 0 },
                                { 0, 2, 0, 0 },
                                { 0, 2, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 0, 0, 0 },
                                { 0, 2, 0, 0 },
                                { 0, 2, 2, 2 },
                                { 0, 0, 0, 0 }
                });
                brickMatrix.add(new int[][] {
                                { 0, 0, 2, 0 },
                                { 0, 0, 2, 0 },
                                { 0, 2, 2, 0 },
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
