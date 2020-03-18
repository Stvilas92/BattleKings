package com.example.battlekings.DrawObjects.gameBars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.example.battlekings.Utils.BitmapManager;

/**
 * Bar located on the down part of the screen. This bar show the actions and data of each object,
 * human , nature or build.
 * Contains buttons to do different actions.
 * The last button always close this bar.
 */
public class DrawActionsBar {
    /**
     * initY init screen point y to start draw the bar
     * screenHeight height of the screen
     */
    private int initY,screenHeight;
    /**
     * bitmapButton bitmap of  the bar buttons
     * bitmapButtonPressed bitmap of  the bar buttons pressed
     */
    private Bitmap bitmapButton,bitmapButtonPressed;
    /** Used to paint the text on the bar */
    private Paint paint;
    /** Rect of the total bar surface*/
    private Rect rectToDraw;
    /**
     * Different data of the game that will be saved on the app database
     */
    private int humansCreated,humansDead,enemiesDead;

    public DrawActionsBar(int initY, int boxSizeX, int boxSizeY, int screenWidth, int screenHeight, Context context) {
        this.humansCreated = 0;
        this.humansDead = 0;
        this.enemiesDead = 0;
        this.initY = initY;
        this.screenHeight = screenHeight;
//        this.bitmap = BitmapManager.getBitmapFromAssets("BarIcons/panel_brown.png",context);
//        this.bitmap = BitmapManager.scale(this.bitmap, screenWidth,screenHeight-initY);
        this.bitmapButton = BitmapManager.getBitmapFromAssets("BarIcons/btn_not_pressed.png",context);
        this.bitmapButton = BitmapManager.scale(this.bitmapButton, (int)(boxSizeX),(int)(boxSizeY));
        this.bitmapButtonPressed = BitmapManager.getBitmapFromAssets("BarIcons/btn_pressed.png",context);
        this.bitmapButtonPressed = BitmapManager.scale(this.bitmapButtonPressed, (int)(boxSizeX),(int)(boxSizeY));
        paint = new Paint();
        paint.setColor(Color.BLACK);
        rectToDraw = new Rect(0,initY,screenWidth,screenHeight);
    }

    /**
     * The background of the DrawActionsBar
     * @param c canvas
     */
    public void draw(Canvas c){
        if(c != null) {
//            c.drawBitmap(bitmap, 0, initY, null);
            c.drawRect(rectToDraw,paint);
        }
    }

    /**
     * Get top of the DrawActionsBar
     * @return Top of the DrawActionsBar
     */
    public int getInitY() {
        return initY;
    }

    /**
     * Get screen height of the device
     * @return screen height of the device
     */
    public int getScreenHeight() {
        return screenHeight;
    }


    /**
     * Get bitmap of the DrawActionsBar button
     * @return bitmap of the DrawActionsBar button
     */
    public Bitmap getBitmapButton() {
        return bitmapButton;
    }

    /**
     * Get bitmap of the DrawActionsBar button when it´s pressed
     * @return bitmap of the DrawActionsBar button when it´s pressed
     */
    public Bitmap getBitmapButtonPressed() {
        return bitmapButtonPressed;
    }

    /**
     * Set the total humans created until init game
     * @param humansCreated total humans created until init game
     */
    public void setHumansCreated(int humansCreated) {
        this.humansCreated = humansCreated;
    }

    /**
     * Set the total humans dead until init game
     * @param humansDead total humans dead until init game
     */
    public void setHumansDead(int humansDead) {
        this.humansDead = humansDead;
    }

    /**
     * Set the total enemies dead until init game
     * @param enemiesDead total humans dead until init game
     */
    public void setEnemiesDead(int enemiesDead) {
        this.enemiesDead = enemiesDead;
    }

    /**
     * Get the total humans created until init game
     * @return  humansCreated total humans created until init game
     */
    public int getHumansCreated() {
        return humansCreated;
    }

    /**
     * Get the total humans dead until init game
     * @return  humansCreated total humans dead until init game
     */
    public int getHumansDead() {
        return humansDead;
    }

    /**
     * Get the total enemies dead until init game
     * @return  humansCreated total enemies dead until init game
     */
    public int getEnemiesDead() {
        return enemiesDead;
    }
}
