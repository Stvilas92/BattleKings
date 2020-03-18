package com.example.battlekings.Database;

/**
 * All data that is save on the sqlite database
 */
public class PlayerData {
    /**
     * Different types of data saved from the game.
     * This data will be saved on the SQLite Database
     */
    private int unitsCreated,unitsLoss,unitsDestroyed,
        resourcesCollected,buildsCreated,buildsDestroyed,buildsLoss;

    public PlayerData(){}

    public PlayerData( int unitsCreated, int unitsLoss, int unitsDestroyed, int resourcesCollected, int buildsCreated, int buildsDestroyed, int buildsLoss) {
        this.unitsCreated = unitsCreated;
        this.unitsLoss = unitsLoss;
        this.unitsDestroyed = unitsDestroyed;
        this.resourcesCollected = resourcesCollected;
        this.buildsCreated = buildsCreated;
        this.buildsDestroyed = buildsDestroyed;
        this.buildsLoss = buildsLoss;
    }

    /**
     * Get total units created on the game
     * @return units created on the game
     */
    public int getUnitsCreated() {
        return unitsCreated;
    }

    /**
     * Set total units created on the game
     * @param unitsCreated units created on the game
     */
    public void setUnitsCreated(int unitsCreated) {
        this.unitsCreated = unitsCreated;
    }

    /**
     * Get total units loss on the game
     * @return units loss on the game
     */
    public int getUnitsLoss() {
        return unitsLoss;
    }

    /**
     * Set total units loss on the game
     * @param unitsLoss units loss on the game
     */
    public void setUnitsLoss(int unitsLoss) {
        this.unitsLoss = unitsLoss;
    }

    /**
     * Get total units destroyed on the game
     * @return units destroyed on the game
     */
    public int getUnitsDestroyed() {
        return unitsDestroyed;
    }

    /**
     * Set total units destroyed on the game
     * @param unitsDestroyed units destroyed on the game
     */
    public void setUnitsDestroyed(int unitsDestroyed) {
        this.unitsDestroyed = unitsDestroyed;
    }

    /**
     * Get all the resources collected on the game
     * @return resources collected on the game
     */
    public int getResourcesCollected() {
        return resourcesCollected;
    }

    /**
     * Set all the resources collected on the game
     * @param resourcesCollected resources collected on the game
     */
    public void setResourcesCollected(int resourcesCollected) {
        this.resourcesCollected = resourcesCollected;
    }

    /**
     * Get total builds created on the game
     * @return builds created on the game
     */
    public int getBuildsCreated() {
        return buildsCreated;
    }

    /**
     * Set total builds created on the game
     * @param buildsCreated builds created on the game
     */
    public void setBuildsCreated(int buildsCreated) {
        this.buildsCreated = buildsCreated;
    }

    /**
     * Get total builds destroyed on the game
     * @return builds destroyed on the game
     */
    public int getBuildsDestroyed() {
        return buildsDestroyed;
    }

    /**
     * Set total builds destroyed on the game
     * @param buildsDestroyed builds destroyed on the game
     */
    public void setBuildsDestroyed(int buildsDestroyed) {
        this.buildsDestroyed = buildsDestroyed;
    }

    /**
     * Get total builds loss on the game
     * @return builds loss on the game
     */
    public int getBuildsLoss() {
        return buildsLoss;
    }

    /**
     * Set total builds loss on the game
     * @param buildsLoss builds loss on the game
     */
    public void setBuildsLoss(int buildsLoss) {
        this.buildsLoss = buildsLoss;
    }
}
