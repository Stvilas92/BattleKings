package com.example.battlekings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battlekings.Screen.GameView;
import com.example.battlekings.Utils.Options;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int LAYOUT_MAIN = R.layout.menu_main;
    private static final int LAYOUT_PLAY = R.layout.menu_play;
    private static final int LAYOUT_OPTIONS = R.layout.menu_options;
    private static final int LAYOUT_PROFILE = R.layout.menu_profile;
    private static final int LAYOUT_CREDITS = R.layout.menu_credits;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    private MediaPlayer mediaPlayer;
    private SharedPreferences preferences;
    private Options options;
    private int actualLayout = LAYOUT_MAIN;
    private Resources res;
    private BD bd;
    private ImageView imageView;
    private AlertDialog.Builder builder;
    private TextView tvUsernameValue;
    private AudioManager audioManager;
    private SoundPool soundEffects;
    private int btnSound,volume;
    private MainActivity mainActivity;
    private boolean screnOrientation = false;
    private GameView game;
    private boolean flagInit = false,gameRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog();
        preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        options = new Options(false,false,Language.ENGLISH);
        getOptionsFromPreferences(preferences);
        setContentView(actualLayout);

        //Audio
        res = this.getResources();
        mediaPlayer = MediaPlayer.create(this,R.raw.main_music);
        audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        int v=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(v/2,v/2);

        bd= new BD(this,"game",null,1);
        SQLiteDatabase db = bd.getWritableDatabase();
        if(db != null){
            PlayerData data = new PlayerData(10,5,9,124,3,7,2);
            bd.putData(db,data);
            db.close();
        }

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
        mediaPlayer.start();
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        setFullScreen();
        if(flagInit){
            initGame();
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

                setScrenOrientation(true);
//                game = new GameView(getApplicationContext(),mainActivity);
                flagInit = true;
//                setContentView(game);
//                game.setKeepScreenOn(true);

//                if(bd != null) {
//                    PlayerData data = new PlayerData(10,5,9,124,3,7,2);
//                    bd.putData(bd.getWritableDatabase(),data);
//                }
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
        tvUsernameValue = findViewById(R.id.txvUsernameValue);

        imageView = findViewById(R.id.imageView);
//        imageView.setImageDrawable(getDrawable(Android));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        ImageView imgAddUsername = findViewById(R.id.btnChangeName);
        imgAddUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
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
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        preferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        getOptionsFromPreferences(preferences);
        if(!mediaPlayer.isPlaying() && options.isMusic()) {
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
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        preferences.edit().putBoolean("vibration",options.isVibration()).apply();
        preferences.edit().putBoolean("music",options.isMusic()).apply();
        preferences.edit().putString("language",options.getLanguage().toString()).apply();
        preferences.edit().putBoolean("initGame",flagInit).apply();
        preferences.edit().putBoolean("gameRunning",gameRunning).apply();
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
        this.flagInit = preferences.getBoolean("initGame",false);
        this.gameRunning = preferences.getBoolean("gameRunning",false);
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
        builder.setTitle("Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvUsernameValue.setText(input.getText().toString());
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
        gameRunning = true;
    }
}
