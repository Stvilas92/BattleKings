package com.example.battlekings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.battlekings.Database.BD;
import com.example.battlekings.Database.PlayerData;
import com.example.battlekings.Screen.GameView;
import com.example.battlekings.Utils.Language;
import com.example.battlekings.Utils.Options;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Main Class of the application. Here are all the menus, configuration, user information, credits,
 * tutorials and the way to init a new game.
 */
public class MainActivity extends AppCompatActivity {
    /**   Used to chargue Layout menu_main */
    private static final int LAYOUT_MAIN = R.layout.menu_main;
    /**   Used to chargue Layout menu_play */
    private static final int LAYOUT_PLAY = R.layout.menu_play;
    /**   Used to chargue Layout menu_options */
    private static final int LAYOUT_OPTIONS = R.layout.menu_options;
    /**   Used to chargue Layout menu_profile */
    private static final int LAYOUT_PROFILE = R.layout.menu_profile;
    /**   Used to chargue Layout menu_credits */
    private static final int LAYOUT_CREDITS = R.layout.menu_credits;
    /**   Used to chargue Layout tutoria */
    private static final int LAYOUT_TUTORIAL = R.layout.tutoria;
    /**   Used to chargue Layout video */
    private static final int LAYOUT_VIDEO = R.layout.video;
    /**   Used to chargue Layout game_description */
    private static final int LAYOUT_DESCRIPTION = R.layout.game_description;
    /**   Used to chargue the phone camera activity */
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    /**   Used to chargue tutorial type move */
    private static final int TUTORIAL_MOVE = 1;
    /**   Used to chargue tutorial type create */
    private static final int TUTORIAL_CREATE = 2;
    /**   Used to chargue tutorial type action */
    private static final int TUTORIAL_ACTION= 3;

    /**   Default text of text view username on profile layout */
    private String username = "username";
    /**   Used to play the music on the menus */
    private MediaPlayer mediaPlayer;
    /**   Used to save specified data of the game */
    private SharedPreferences preferences;
    /**   Options of the game. Include volume,vibration and language */
    private Options options;
    /**   Layout that shows on the screen*/
    private int actualLayout = LAYOUT_MAIN;
    /**   App resources*/
    private Resources res;
    /**   Used to manage the game database*/
    private BD bd;
    /**   Used to save the photo toke from camera on user profile*/
    private ImageView imageView;
    /**   Used to save the user name on user profile*/
    private AlertDialog.Builder builder;
    /**   Used to show the user name on user profile*/
    private TextView tvUsernameValue;
    /**   Used to play buttons sounds on each touch*/
    private AudioManager audioManager;
    /**   Used to play buttons sounds on each touch*/
    private SoundPool soundEffects;
    /**   Used to control the volume of each sound*/
    private int btnSound,volume;
    /**   Actual main activity*/
    private MainActivity mainActivity;
    /**   Used to manage the screen orientarion. false - portait, true - landscape*/
    private boolean screnOrientation = false;
    /**   Control the game when its init. All the game; movement, clicks, drawing, memory etc*/
    private GameView game;
    /**   Used to mange if the game is init whe onResume and onPause are called*/
    private boolean flagInit = false,gameRunning = false;

    /**
     * External file to save a photo toke from camera
     */
    private File f;
    private  int imageByteArraySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog();
        File dir = Environment.getExternalStorageDirectory();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            checkPermision();
            f = new File(dir.getAbsolutePath(), "camera.png");
        }

        preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        options = new Options(false,false, Language.ENGLISH);
        getOptionsFromPreferences();
        setContentView(actualLayout);

        //Audio
        res = this.getResources();
        mediaPlayer = MediaPlayer.create(this,R.raw.main_music);
        audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        int v=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(v/2,v/2);

        //Main menu
        inicializateComponentsMain();
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
        volume = 1;
        loadImage();
        setFullScreen();


        if(flagInit){
            if(gameRunning && screnOrientation){
                setScrenOrientation(true);
                initGame();
                return;
            }else{
                setContentViewMain();
            }
            flagInit = false;
        }
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
        setContentView(actualLayout);
        inicializateComponentsMain();
    }

    /**
     * Inicializate components of the view 'menu_main'
     */
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

    /**
     * Inicializate components of the view 'menu_options'
     */
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
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }else{
            btnMusic.setText(R.string.menu_options_music_off);
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.res.Configuration configuration;
                DisplayMetrics dm = res.getDisplayMetrics();
                configuration = new Configuration(res.getConfiguration());
                if(options.getLanguage().equals(Language.SPANISH)){
                    options.setLanguage(Language.ENGLISH);
                    configuration.setLocale(new Locale("en"));
                }else if(options.getLanguage().equals(Language.ENGLISH)){
                    options.setLanguage(Language.SPANISH);
                    configuration.setLocale(new Locale("es"));
                }

                if(options.isVibration()) {
                    vibrate();
                }
                //TODO Da error
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
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                    }
                }else{
                    btnMusic.setText(R.string.menu_options_music_off);
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
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

    /**
     * Inicializate components of the view 'menu_play'
     */
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
                gameRunning = true;
                flagInit = true;
                setScrenOrientation(true);
//                game = new GameView(getApplicationContext(),mainActivity);
//                setContentView(game);
//                game.setKeepScreenOn(true);

//                if(bd != null) {
//                    PlayerData data = new PlayerData(10,5,9,124,3,7,2);
//                    bd.putData(bd.getWritableDatabase(),data);
//                }
            }
        });

        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_TUTORIAL);
                setContentView(actualLayout);
                inicializateComponentsTutorial();
            }
        });

        btnNewGame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });

        btnTutorial.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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

    /**
     * Inicializate components of the view 'menu_credits'
     */
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

    /**
     * Inicializate components of the view 'tutorial'
     */
    private void inicializateComponentsTutorial(){
        setFullScreen();
        final Button btnBack = findViewById(R.id.btnBackTutorial);
        final Button btnObjective = findViewById(R.id.btnObjective);
        final Button btnMove = findViewById(R.id.btnHowToMove);
        final Button btnActChar = findViewById(R.id.btnHowToAction);
        final Button btnActBuild = findViewById(R.id.btnHowToActionBuild);

        btnObjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_DESCRIPTION);
                setContentView(actualLayout);
                inicializateDescription();
            }
        });

        btnObjective.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });

        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_VIDEO);
                setContentView(actualLayout);
                inicializateVideo(TUTORIAL_MOVE);
            }
        });

        btnMove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });

        btnActChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_VIDEO);
                setContentView(actualLayout);
                inicializateVideo(TUTORIAL_ACTION);
            }
        });

        btnActChar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });

        btnActBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(LAYOUT_VIDEO);
                setContentView(actualLayout);
                inicializateVideo(TUTORIAL_CREATE);
            }
        });

        btnActBuild.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });

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

    /**
     * Inicializate components of the view 'menu_profile'
     */
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

        TextView tvResourcesCollected = findViewById(R.id.txvResourcesCollectedValue);
        tvResourcesCollected.setText(""+data.getResourcesCollected());
        TextView tvUnitsCreated = findViewById(R.id.txvUnitsCreatedValue);
        tvUnitsCreated.setText(""+data.getUnitsCreated());
        TextView tvUnitsLoss = findViewById(R.id.txvUnitsLossValue);
        tvUnitsLoss.setText(""+data.getUnitsLoss());
        TextView tvUnitsDestroyed = findViewById(R.id.txvUnitsDestryedValue);
        tvUnitsDestroyed.setText(""+data.getUnitsDestroyed());
        tvUsernameValue = findViewById(R.id.txvUsernameValue);
        tvUsernameValue.setText(username);

        imageView = findViewById(R.id.imageView);
//        imageView.setImageDrawable(getDrawable(Android));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                setFullScreen();
            }
        });

        ImageView imgAddUsername = findViewById(R.id.btnChangeName);
        imgAddUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
                setFullScreen();
            }
        });

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

    /**
     * Inicializate components of the view 'video'
     */
    private void inicializateVideo(int tutorialType){
        Button btnBackVideo = findViewById(R.id.btnBackVideo);
        ImageView imgUp = findViewById(R.id.tutorial_image_up);
        ImageView imgDown = findViewById(R.id.tutorial_image_down);
        TextView  txvTutorial = findViewById(R.id.txv_tutorial);

        switch (tutorialType){
            case TUTORIAL_MOVE:
                imgUp.setImageDrawable(getDrawable(R.drawable.tutorial_zone_selected));
                imgDown.setImageDrawable(null);
                txvTutorial.setText(getResources().getText(R.string.tutorial_move_text));
                break;

            case TUTORIAL_CREATE:
                imgUp.setImageDrawable(getDrawable(R.drawable.tutorial_seleccionar_edificio));
                imgDown.setImageDrawable(getDrawable(R.drawable.tutorial_seleccionar_edificio_2));
                txvTutorial.setText(getResources().getText(R.string.tutorial_create_text));
                break;

            case TUTORIAL_ACTION:
                imgUp.setImageDrawable(getDrawable(R.drawable.tutorial_enemy_clicked));
                imgDown.setImageDrawable(getDrawable(R.drawable.turorial_on_action));
                txvTutorial.setText(getResources().getText(R.string.tutorial_action_text));
                break;
        }

        setFullScreen();

        btnBackVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBackVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
            }
        });
    }

    /**
     * Inicializate components of the view 'video'
     */
    private void inicializateDescription(){
        Button btnBackVideo = findViewById(R.id.btnBAckDescription);

        btnBackVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBackVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnTouch(v,event);
                return false;
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
        }else if(actualLayout == LAYOUT_TUTORIAL){
            changeLayout(LAYOUT_PLAY);
            setContentView(LAYOUT_PLAY);
            inicializateComponentsPlay();
            if(options.isVibration()) {
                vibrate();
            }
        }else if(actualLayout == LAYOUT_VIDEO || actualLayout == LAYOUT_DESCRIPTION){
            changeLayout(LAYOUT_TUTORIAL);
            setContentView(LAYOUT_TUTORIAL);
            inicializateComponentsTutorial();
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
        volume = 1;
        preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        getOptionsFromPreferences();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStorageDirectory();
            f = new File(dir.getAbsolutePath(), "camera.png");
            loadImage();
        }
        if( options.isMusic()) {
                mediaPlayer.start();
        }

        setFullScreen();
        if(gameRunning){
            setScrenOrientation(true);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        volume =1;
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        preferences.edit().putBoolean("vibration",options.isVibration()).apply();
        preferences.edit().putBoolean("music",options.isMusic()).apply();
        preferences.edit().putString("language",options.getLanguage().toString()).apply();
        preferences.edit().putBoolean("initGame",flagInit).apply();
        preferences.edit().putBoolean("gameRunning",gameRunning).apply();
        preferences.edit().putBoolean("screenOrientation",screnOrientation).apply();
        preferences.edit().putString("username",username).apply();
    }

    /**
     * Get the option class and variable values save on SharedPreferences
     */
    private void getOptionsFromPreferences(){
        this.options.setVibration( preferences.getBoolean("vibration",false));
        this.options.setMusic( preferences.getBoolean("music",false));
        String language = preferences.getString("language","ENGLISH");
        this.options.setLanguage(Language.valueOf(language));
        this.flagInit = preferences.getBoolean("initGame",false);
        this.gameRunning = preferences.getBoolean("gameRunning",false);
        this.screnOrientation = preferences.getBoolean("screenOrientation",false);
        this.username = preferences.getString("username","Username");
    }

    /**
     * Changues the button view imgage if a button is pressed
     * @param button button view
     * @param theMotion motion, ACTION_UP or ACTION_DOWN
     * @return always true
     */
    public boolean btnTouch(View button , MotionEvent theMotion ) {
        if(options.isMusic()) {
            soundEffects.play(btnSound, volume, volume, 1, 0, 1);
        }

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

    /**
     * Start the android dprefedefined camera activity
     */
    private void dispatchTakePictureIntent() {
        mediaPlayer.pause();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    /**
     * Take image from the camera and put the result on 'imageView'
     * @param requestCode request code of the activity
     * @param resultCode result code of activity
     * @param data Intent of the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    /**
     * Create a text dialog and put the result on the text box 'tvUsernameValue'
     */
    private void createDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.menu_change_name));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = input.getText().toString();
                tvUsernameValue.setText(username);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
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

    /**
     * Init the game charging the surface view to draw the game graphics
     */
    private void initGame(){
        game = new GameView(getApplicationContext(),mainActivity);
        setContentView(game);
        game.setKeepScreenOn(true);
    }

    /**
     * Set boolean gameRunning. Indicates if the game is running or no.
     * @param gameRunning  boolean gameRunning
     */
    public void setGameRunning(boolean gameRunning){
        this.gameRunning = gameRunning;
    }

    /**
     * Get URI of a file from the project resources directory.
     * @param mediaName
     * @return
     */
    private String getMedia(String mediaName) {
        return "android.resource://" + getPackageName() +
                "/raw/" + mediaName;
    }


    public void saveImage(){
        checkPermision();
        Drawable drawable = imageView.getDrawable();
        FileOutputStream outputStream = null;
        byte[] bitmapdata = null;

        try {
            if (drawable != null) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                bitmapdata = stream.toByteArray();
                bitmap.recycle();
            }
            outputStream = new FileOutputStream(f);
            outputStream.write(bitmapdata);
            imageByteArraySize = bitmapdata.length;
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadImage(){
        checkPermision();
        Drawable drawable = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(f);
            fileInputStream.read();

            int cursor,index = 0;
            byte[] imageByte = new byte[imageByteArraySize];
            while ((cursor =fileInputStream.read()) != -1) {
                imageByte[index]= (byte) cursor;
            }

            drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length));
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkPermision(){
        if(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return;
        }else{
            int requestCode=1;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        }
    }

}
