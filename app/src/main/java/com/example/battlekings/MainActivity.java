package com.example.battlekings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int LAYOUT_MAIN = R.layout.menu_main;
    private static final int LAYOUT_PLAY = R.layout.menu_play;
    private static final int LAYOUT_OPTIONS = R.layout.menu_options;
    private static final int LAYOUT_PROFILE = R.layout.menu_profile;
    private static final int LAYOUT_CREDITS = R.layout.menu_credits;

    SharedPreferences preferences;
    private Options options;
    private int actualLayout = LAYOUT_MAIN;
    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        options = new Options(false,false,Language.ENGLISH);
        getOptionsFromPreferences(preferences);
//        options = new Options(this);
//        options.getOptionsFromProperties();
        setContentView(actualLayout);
        res = this.getResources();

        inicializateComponentsMain();
    }

    private void changeLayout(int layoutIndex){
        actualLayout = layoutIndex;
    }

    private void inicializateComponentsMain(){
        Button btnPlay = findViewById(R.id.btnNewGame_main);
        Button btnCredits = findViewById(R.id.btnBackCredits);
        Button btnOptions = findViewById(R.id.btnOption);
        Button btnProfile =  findViewById(R.id.btnProfile);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_PLAY);
                setContentView(actualLayout);
                inicializateComponentsPlay();
                if(options.isVibration()) {
                    vibrate();
                }
            }
        });

        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_CREDITS);
                setContentView(actualLayout);
                inicializateComponentsCredits();
                if(options.isVibration()) {
                    vibrate();
                }
            }
        });

        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_OPTIONS);
                setContentView(actualLayout);
                inicializateComponentsOptions();
                if(options.isVibration()) {
                    vibrate();
                }
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_PROFILE);
                setContentView(actualLayout);
                inicializateComponentsProfile();
                if(options.isVibration()) {
                    vibrate();
                }
            }
        });
    }

    private void inicializateComponentsOptions(){
        final Button btnMusic = findViewById(R.id.btnMusic);
        final Button btnVibration = findViewById(R.id.btnVibration);
        final Button btnLanguage = findViewById(R.id.btnLanguage);
        final Button btnBack = findViewById(R.id.btnBackOptions);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(options.isVibration()){
            btnVibration.setText(R.string.menu_options_vibration_on);
        }else{
            btnVibration.setText(R.string.menu_options_vibration_off);
        }

        if(options.isMusic()){
            btnMusic.setText(R.string.menu_options_music_on);
        }else{
            btnMusic.setText(R.string.menu_options_music_off);
        }

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration configuration;
                configuration = new Configuration(res.getConfiguration());
                if(options.getLanguage().equals(Language.ESPAÑOL)){
                    options.setLanguage(Language.ENGLISH);
                    configuration.setLocale(new Locale("en"));
                }else if(options.getLanguage().equals(Language.ENGLISH)){
                    options.setLanguage(Language.ESPAÑOL);
                    configuration.setLocale(new Locale("es"));
                }

                if(options.isVibration()) {
                    vibrate();
                }
                //TODO pendiente de hacer
                getApplicationContext().createConfigurationContext(configuration);
            }
        });

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.setMusic(!options.isMusic());
                if(options.isMusic()){
                    btnMusic.setText(R.string.menu_options_music_on);
                }else{
                    btnMusic.setText(R.string.menu_options_music_off);
                }

                if(options.isVibration()) {
                    vibrate();
                }
            }
        });

        btnVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.setVibration(!options.isVibration());
                if(options.isVibration()){
                    vibrate();
                    btnVibration.setText(R.string.menu_options_vibration_on);
                }else{
                    btnVibration.setText(R.string.menu_options_vibration_off);
                }
            }
        });
    }

    private void inicializateComponentsPlay(){
        Button btnNewGame = findViewById(R.id.btnNewGame_main);
        Button btnTutorial = findViewById(R.id.btnTutorial);
        Button btnBack = findViewById(R.id.btnBackPlay);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isVibration()) {
                    vibrate();
                }
            }
        });

        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isVibration()) {
                    vibrate();
                }
            }
        });
    }

    private void inicializateComponentsCredits(){
        TextView txvCredits = findViewById(R.id.txvCredits);
        String[] creditsTotal = res.getStringArray(R.array.list_credits);
        String text = "";
        for (int i = 0; i < creditsTotal.length; i++) {
            text += creditsTotal[i]+"\n";
        }
        txvCredits.setText(text);

        final Button btnBack = findViewById(R.id.btnBackCredits);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void inicializateComponentsProfile(){
        final Button btnBack = findViewById(R.id.btnBackProfile);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(actualLayout == LAYOUT_CREDITS || actualLayout == LAYOUT_OPTIONS ||
                actualLayout == LAYOUT_PLAY || actualLayout == LAYOUT_PROFILE){
            changeLayout(LAYOUT_MAIN);
            setContentView(LAYOUT_MAIN);
            inicializateComponentsMain();
            if(options.isVibration()) {
                vibrate();
            }
        }
    }

    private void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }

    private void setFullScreen(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreen();
        preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        getOptionsFromPreferences(preferences);
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences.edit().putBoolean("vibration",options.isVibration()).apply();
        preferences.edit().putBoolean("music",options.isMusic()).apply();
        preferences.edit().putString("language",options.getLanguage().toString()).apply();
    }

    private void getOptionsFromPreferences(SharedPreferences sharedPreferences){
        this.options.setVibration( preferences.getBoolean("vibration",false));
        this.options.setMusic( preferences.getBoolean("music",false));
        String language = preferences.getString("language","ENGLISH");
        this.options.setLanguage(Language.valueOf(language));
    }
}
