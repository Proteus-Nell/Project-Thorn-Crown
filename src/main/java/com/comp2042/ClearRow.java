package com.comp2042;

/**
 * Represents the result of clearing rows.
 * Contains the number of lines removed, the new board matrix, and the score
 * bonus.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructs a new ClearRow object.
     *
     * @param linesRemoved The number of lines cleared.
     * @param newMatrix    The updated board matrix.
     * @param scoreBonus   The score bonus awarded.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines removed.
     *
     * @return The count of cleared lines.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets the new board matrix after clearing rows.
     *
     * @return A copy of the updated board matrix.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus for the cleared rows.
     *
     * @return The score bonus.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
