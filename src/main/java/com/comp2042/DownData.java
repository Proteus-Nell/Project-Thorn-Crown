package com.comp2042;

/**
 * Data class holding the result of a downward movement.
 * Contains information about cleared rows and the current view data.
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a new DownData object.
     *
     * @param clearRow The result of clearing rows (if any).
     * @param viewData The current view data of the brick.
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the cleared row information.
     *
     * @return The {@link ClearRow} object, or null if no rows were cleared.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the view data.
     *
     * @return The {@link ViewData} object.
     */
    public ViewData getViewData() {
        return viewData;
    }
}
