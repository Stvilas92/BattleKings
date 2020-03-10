package com.example.battlekings.Utils;

import android.content.Context;

import com.example.battlekings.Language;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Options {
    private static final String PROPERTIES_FILENAME = "application.properties";
    private boolean vibration,music;
    private Language language;
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
