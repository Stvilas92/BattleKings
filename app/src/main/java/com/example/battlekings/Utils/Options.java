package com.example.battlekings.Utils;

import android.content.Context;

public class Options {
    /**
     * vibration false - no vibration, true - vibrate
     * music false - music off, true - music on
     */
    private boolean vibration,music;
    /** Game language*/
    private Language language;
    /** Application context*/
    private Context context ;

    public Options(Context context){
        this.context = context;
    }

    public Options(boolean vibration, boolean music, Language language) {
        this.context = context;
        this.vibration = vibration;
        this.music = music;
        this.language = language;
    }

    /**
     * Get if vibration is activated
     * @return boolean, true if vibration is activated, else, false
     */
    public boolean isVibration() {
        return vibration;
    }

    /**
     * St if vibration is activated
     * @param vibration boolean, true if vibration is activated, else, false
     */
    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    /**
     * Get if music is activated
     * @return boolean, true if music is activated, else, false
     */
    public boolean isMusic() {
        return music;
    }

    /**
     * Set if music is activated
     * @param music boolean, true if music is activated, else, false
     */
    public void setMusic(boolean music) {
        this.music = music;
    }

    /**
     * Get the actual language
     * @return actual language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Set the actual language
     * @param language  actual language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }
}
