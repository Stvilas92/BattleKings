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

    public void  getOptionsFromProperties(){
        Properties prop = new Properties();

        try (InputStream inputStream = new FileInputStream(PROPERTIES_FILENAME)){
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + PROPERTIES_FILENAME + "' not found in the classpath");
            }

            this.vibration = Boolean.parseBoolean(prop.getProperty("vibration"));
            this.music  = Boolean.parseBoolean(prop.getProperty("music"));

            if(prop.getProperty("language").equals("Español")){
                this.language = Language.ESPAÑOL;
            }else if(prop.getProperty("language").equals("English")){
                this.language = Language.ENGLISH;
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
