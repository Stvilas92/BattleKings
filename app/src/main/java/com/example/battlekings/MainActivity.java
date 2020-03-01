package com.example.battlekings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int LAYOUT_MAIN = R.layout.menu_main;
    private static final int LAYOUT_PLAY = R.layout.menu_play;
    private static final int LAYOUT_OPTIONS = R.layout.menu_options;
    private static final int LAYOUT_PROFILE = R.layout.menu_profile;
    private static final int LAYOUT_CREDITS = R.layout.menu_credits;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private SharedPreferences preferences;
    private Options options;
    private int actualLayout = LAYOUT_MAIN;
    private Resources res;
    private BD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        options = new Options(false,false,Language.ENGLISH);
        getOptionsFromPreferences(preferences);
        setContentView(actualLayout);
        res = this.getResources();
        mediaPlayer = MediaPlayer.create(this,R.raw.main_music);
        audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        int v=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(v/2,v/2);

        BD bd= new BD(this,"game",null,1);
        SQLiteDatabase db = bd.getWritableDatabase();
        if(db != null){
//            PlayerData data = new PlayerData(10,5,9,124,3,7,2);
//            bd.putData(db,data);
            db.close();
        }

        inicializateComponentsMain();
        mediaPlayer.start();
    }

    private void changeLayout(int layoutIndex){
        actualLayout = layoutIndex;
    }

    private void inicializateComponentsMain(){
        Button btnPlay = findViewById(R.id.btnSurvive);
        Button btnCredits = findViewById(R.id.btnCredits);
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

        btnPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

        btnCredits.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

        btnOptions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

        btnProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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
        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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
                android.content.res.Configuration configuration;
                DisplayMetrics dm = res.getDisplayMetrics();
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
                res.updateConfiguration(configuration,dm);
            }
        });

        btnLanguage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

        btnMusic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

        btnVibration.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });
    }

    private void inicializateComponentsPlay(){
        Button btnNewGame = findViewById(R.id.btnSurvive);
        Button btnTutorial = findViewById(R.id.btnTutorial);
        Button btnBack = findViewById(R.id.btnBackPlay);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isVibration()) {
                    vibrate();
                }

                PlayerData data = new PlayerData(10,5,9,124,3,7,2);
                bd.putData(bd.getWritableDatabase(),data);
            }
        });

        btnNewGame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

        btnTutorial.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });
    }

    private void inicializateComponentsProfile(){
        final Button btnBack = findViewById(R.id.btnBackProfile);
        bd = new BD(this,"game",null,1);
        SQLiteDatabase readble = bd.getReadableDatabase();
        PlayerData data = new PlayerData();
        if(readble != null) {
             data = bd.getTotalData(bd.getReadableDatabase());

             readble.close();
        }else{
            Toast.makeText(getApplicationContext(),"Player data is not available at the moment",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        TextView tvBuildsCreated = findViewById(R.id.txvBuildingsCreatedValue);
        tvBuildsCreated.setText(""+data.getBuildsCreated());
        TextView tvBuildsLoss = findViewById(R.id.txvBuildingsLossValue);
        tvBuildsLoss.setText(""+data.getBuildsLoss());
        TextView tvBuildsDestroyed = findViewById(R.id.txvBuildingsDestroyedValue);
        tvBuildsDestroyed.setText(""+data.getBuildsDestroyed());
        TextView tvResourcesCollected = findViewById(R.id.txvResourcesCollectedValue);
        tvResourcesCollected.setText(""+data.getResourcesCollected());
        TextView tvUnitsCreated = findViewById(R.id.txvUnitsCreatedValue);
        tvUnitsCreated.setText(""+data.getUnitsCreated());
        TextView tvUnitsLoss = findViewById(R.id.txvUnitsLossValue);
        tvUnitsLoss.setText(""+data.getUnitsLoss());
        TextView tvUnitsDestroyed = findViewById(R.id.txvUnitsDestryedValue);
        tvUnitsDestroyed.setText(""+data.getUnitsDestroyed());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

    public boolean btnTouch(View button , MotionEvent theMotion ) {
        switch ( theMotion.getAction() ) {
            case MotionEvent.ACTION_UP:
                switch (button.getId()){
                    case R.id.btnBackCredits:
                    case R.id.btnBackOptions:
                    case R.id.btnBackPlay:
                    case R.id.btnBackProfile:
//                        button.setBackground(getDrawable(R.drawable.green_button01));
                        break;

                    case R.id.btnSurvive:
                    case R.id.btnLanguage:
                        button.setBackground(getDrawable(R.drawable.blue_button00));
                        break;

                    case R.id.btnMusic:
                    case R.id.btnCredits:
                        button.setBackground(getDrawable(R.drawable.yellow_button00));
                        break;

                    case R.id.btnVibration:
                    case R.id.btnTutorial:
                    case R.id.btnOption:
                        button.setBackground(getDrawable(R.drawable.green_button00));
                        break;

                    case R.id.btnProfile:
                        button.setBackground(getDrawable(R.drawable.grey_button00));
                        break;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                switch (button.getId()){
                    case R.id.btnBackCredits:
                    case R.id.btnBackOptions:
                    case R.id.btnBackPlay:
                    case R.id.btnBackProfile:
//                        button.setBackground(getDrawable(R.drawable.green_button01));
                        break;

                    case R.id.btnSurvive:
                    case R.id.btnLanguage:
                        button.setBackground(getDrawable(R.drawable.blue_button01));
                        break;

                    case R.id.btnMusic:
                    case R.id.btnCredits:
                        button.setBackground(getDrawable(R.drawable.yellow_button01));
                        break;

                    case R.id.btnVibration:
                    case R.id.btnTutorial:
                    case R.id.btnOption:
                        button.setBackground(getDrawable(R.drawable.green_button01));
                        break;

                    case R.id.btnProfile:
                        button.setBackground(getDrawable(R.drawable.grey_button01));
                        break;
                }
                break;
        }
        return true;
    }
}
