package com.example.battlekings.Screen;

/**
 * Point of the screen
 */
public class PointIndex {
    /**
     * indexX point x of the screen
     * indexY point y of the screen
     */
    int indexX,indexY;

    public PointIndex(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }

    /**
     * Get the index X of the point
     * @return
     */
    public int getIndexX() {
        return indexX;
    }

    /**
     * Get the index Y of the point
     * @return
     */
    public int getIndexY() {
        return indexY;
    }

}
