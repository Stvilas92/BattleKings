package com.example.battlekings;

public class PlayerData {
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


    public int getUnitsCreated() {
        return unitsCreated;
    }

    public void setUnitsCreated(int unitsCreated) {
        this.unitsCreated = unitsCreated;
    }

    public int getUnitsLoss() {
        return unitsLoss;
    }

    public void setUnitsLoss(int unitsLoss) {
        this.unitsLoss = unitsLoss;
    }

    public int getUnitsDestroyed() {
        return unitsDestroyed;
    }

    public void setUnitsDestroyed(int unitsDestroyed) {
        this.unitsDestroyed = unitsDestroyed;
    }

    public int getResourcesCollected() {
        return resourcesCollected;
    }

    public void setResourcesCollected(int resourcesCollected) {
        this.resourcesCollected = resourcesCollected;
    }

    public int getBuildsCreated() {
        return buildsCreated;
    }

    public void setBuildsCreated(int buildsCreated) {
        this.buildsCreated = buildsCreated;
    }

    public int getBuildsDestroyed() {
        return buildsDestroyed;
    }

    public void setBuildsDestroyed(int buildsDestroyed) {
        this.buildsDestroyed = buildsDestroyed;
    }

    public int getBuildsLoss() {
        return buildsLoss;
    }

    public void setBuildsLoss(int buildsLoss) {
        this.buildsLoss = buildsLoss;
    }
}
