package com.example.battlekings.Database;

/**
 * Data from a game ended.
 */
public class BattleResult{
    /**
     * Different types of data saved from the game.
     * This data will be saved on the SQLite Database
     */
    private int resourcesCollected,unitsCreated,unitsDefeated,unitsLoss;

    public BattleResult(int unitsCreated, int unitsLoss, int unitsDestroyed, int resourcesCollected, int buildsCreated, int buildsDestroyed, int buildsLoss, int resourcesCollected1, int unitsCreated1, int unitsDefeated, int unitsLoss1) {
        this.resourcesCollected = resourcesCollected1;
        this.unitsCreated = unitsCreated1;
        this.unitsDefeated = unitsDefeated;
        this.unitsLoss = unitsLoss1;
    }
}
