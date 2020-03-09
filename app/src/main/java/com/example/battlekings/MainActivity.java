package com.example.battlekings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.battlekings.Screen.GameView;
import com.example.battlekings.Utils.Options;

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
    private GameView game;
    private Resources res;
    private boolean screnOrientation = false;
    private MainActivity mainActivity;
    private boolean flagInitGame = false;
    private AudioManager audioManager;
    private SoundPool soundEffects;
    private int btnSound,volume;
    private MediaPlayer mediaPlayer;

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
        mainActivity = this;
        audioManager=(AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if ((android.os.Build.VERSION.SDK_INT) >= 21) {
            SoundPool.Builder spb=new SoundPool.Builder();
            spb.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            spb.setMaxStreams(5);
            this.soundEffects=spb.build();
        } else {
            this.soundEffects=new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.play_game);
        btnSound = soundEffects.load(getApplicationContext(),R.raw.click,1);
        inicializateComponentsMain();
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * Change layaout by a index that is the id of the layout.
     * @param layoutIndex id of the layout
     */
    private void changeLayout(int layoutIndex){
        actualLayout = layoutIndex;
    }

    /**
     * Set the view 'menu_main' and inicializate his components.
     */
    public void setContentViewMain(){
        changeLayout(LAYOUT_MAIN);
        inicializateComponentsMain();
    }

    /**
     * Inicializate components of the view 'menu_main'
     */
    private void inicializateComponentsMain(){
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Button btnPlay = findViewById(R.id.btnNewGame_main);
        Button btnCredits = findViewById(R.id.btnBackCredits);
        Button btnOptions = findViewById(R.id.btnOption);
        Button btnProfile =  findViewById(R.id.btnProfile);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
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
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
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
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
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
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
                changeLayout(LAYOUT_PROFILE);
                setContentView(actualLayout);
                inicializateComponentsProfile();
                if(options.isVibration()) {
                    vibrate();
                }
            }
        });
    }

    /**
     * Inicializate components of the view 'menu_options'
     */
    private void inicializateComponentsOptions(){
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final Button btnMusic = findViewById(R.id.btnMusic);
        final Button btnVibration = findViewById(R.id.btnVibration);
        final Button btnLanguage = findViewById(R.id.btnLanguage);
        final Button btnBack = findViewById(R.id.btnBackOptions);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
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
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
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
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
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
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
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

    /**
     * Inicializate components of the view 'menu_play'
     */
    private void inicializateComponentsPlay(){
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Button btnNewGame = findViewById(R.id.btnNewGame_main);
        Button btnTutorial = findViewById(R.id.btnTutorial);
        Button btnBack = findViewById(R.id.btnBackPlay);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
                onBackPressed();
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
                if(options.isVibration()) {
                    vibrate();
                }

                setScrenOrientation(true);
                game = new GameView(getApplicationContext(),mainActivity);
                setContentView(game);
                game.setKeepScreenOn(true);
            }
        });

        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
                if(options.isVibration()) {
                    vibrate();
                }
            }
        });
    }

    /**
     * Inicializate components of the view 'menu_credits'
     */
    private void inicializateComponentsCredits(){
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
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
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
                onBackPressed();
            }
        });
    }

    /**
     * Inicializate components of the view 'menu_profile'
     */
    private void inicializateComponentsProfile(){
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final Button btnBack = findViewById(R.id.btnBackProfile);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options.isMusic()) {
                    soundEffects.play(btnSound, volume, volume, 1, 0, 1);
                }
                onBackPressed();
            }
        });
    }

    /**
     * Change of view when back button is pressed
     */
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

    /**
     * Activate the vibration during 50 miliseconds
     */
    private void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }

    /**
     * Set full screen mode on the device
     */
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

    /**
     * Get the option class and variable values save on SharedPreferences
     * @param sharedPreferences shared preferenced witch contains Options class variables
     */
    private void getOptionsFromPreferences(SharedPreferences sharedPreferences){
        this.options.setVibration( preferences.getBoolean("vibration",false));
        this.options.setMusic( preferences.getBoolean("music",false));
        String language = preferences.getString("language","ENGLISH");
        this.options.setLanguage(Language.valueOf(language));
    }

    /**
     * Set screen orientation by a boolean variable
     * @param screnOrientation true - landscape , false - portait
     */
    public void setScrenOrientation(boolean screnOrientation) {
        this.screnOrientation = screnOrientation;

        if(screnOrientation){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
