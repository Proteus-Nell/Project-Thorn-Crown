package com.comp2042.model.board;

import com.comp2042.config.GameConfig;
import com.comp2042.model.score.ClearRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for matrix operations.
 * Provides methods for collision detection, matrix copying, merging, and row
 * clearing.
 */
public class MatrixOperations {

    private static final int SCORE_MULTIPLIER_PER_ROW = GameConfig.SCORE_MULTIPLIER_PER_ROW;

    // We don't want to instantiate this utility class
    private MatrixOperations() {

    }

    /**
     * Checks if a brick collides with existing blocks in the matrix.
     *
     * @param matrix The board matrix.
     * @param brick  The brick shape matrix.
     * @param x      The x-coordinate of the brick.
     * @param y      The y-coordinate of the brick.
     * @return true if a collision is detected, false otherwise.
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        // Check each cell of the brick for collision
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                if (brick[col][row] == 0) {
                    continue; // Skip empty cells
                }

                int targetX = x + row;
                int targetY = y + col;
                boolean outOfBounds = checkOutOfBound(matrix, targetX, targetY);
                boolean cellOccupied = !outOfBounds && matrix[targetY][targetX] != 0;

                if (outOfBounds || cellOccupied) {
                    return true; // Collision detected
                }
            }
        }
        return false; // No collision
    }

    /*
     * Check if the target position is out of bounds, resolves a crash bug where the
     * game would crash when the tetronimo is out of bounds.
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        return targetY < 0 || targetY >= matrix.length || // Y out of bounds
                targetX < 0 || targetX >= matrix[targetY].length; // X out of bounds
    }

    /**
     * Creates a deep copy of a 2D integer array.
     *
     * @param original The original matrix.
     * @return A deep copy of the matrix.
     */
    public static int[][] copy(int[][] original) {
        int[][] newMatrix = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] row = original[i];
            int rowLength = row.length;
            newMatrix[i] = new int[rowLength];
            System.arraycopy(row, 0, newMatrix[i], 0, rowLength);
        }
        return newMatrix;
    }

    /**
     * Merges a brick into the board matrix.
     *
     * @param filledFields The current board matrix.
     * @param brick        The brick shape matrix.
     * @param x            The x-coordinate of the brick.
     * @param y            The y-coordinate of the brick.
     * @return A new matrix with the brick merged in.
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] result = copy(filledFields);

        // Place brick cells onto the result matrix
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                if (brick[col][row] != 0) {
                    result[y + col][x + row] = brick[col][row];
                }
            }
        }
        return result;
    }

    /**
     * Checks for and removes full rows from the matrix.
     *
     * @param matrix The board matrix.
     * @return A {@link ClearRow} object containing the result of the operation.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        List<int[]> keptRows = new ArrayList<>();
        int clearedRowCount = 0;
        // Identify and separate complete rows from incomplete rows
        for (int row = 0; row < matrix.length; row++) {
            if (isRowComplete(matrix[row])) {
                clearedRowCount++;
            } else {
                keptRows.add(copyRow(matrix[row]));
            }
        }
        // Build result matrix: empty rows at top, kept rows at bottom
        int[][] resultMatrix = new int[matrix.length][matrix[0].length];
        int startRow = matrix.length - keptRows.size();
        for (int i = 0; i < keptRows.size(); i++) {
            resultMatrix[startRow + i] = keptRows.get(i);
        }
        int scoreBonus = SCORE_MULTIPLIER_PER_ROW * clearedRowCount * clearedRowCount;
        return new ClearRow(clearedRowCount, resultMatrix, scoreBonus);
    }

    // Check if a row is completely filled (no empty cells).
    private static boolean isRowComplete(int[] row) {
        for (int cell : row) {
            if (cell == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create a copy of a single row.
     */
    private static int[] copyRow(int[] row) {
        int[] copy = new int[row.length];
        System.arraycopy(row, 0, copy, 0, row.length);
        return copy;
    }

    /**
     * Creates a deep copy of a list of 2D integer arrays.
     *
     * @param list The original list.
     * @return A deep copy of the list.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
