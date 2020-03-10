package com.example.battlekings.DrawObjects.gameBars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.battlekings.Utils.BitmapManager;

public class DrawActionsBar {
    private int initY,boxSizeX,boxSizeY,screenWidth,screenHeight;
    private Bitmap bitmap,bitmapButton,bitmapButtonPressed;

    public DrawActionsBar(int initY, int boxSizeX, int boxSizeY, int screenWidth, int screenHeight, Context context) {
        this.initY = initY;
        this.boxSizeX = boxSizeX;
        this.boxSizeY = boxSizeY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.bitmap = BitmapManager.getBitmapFromAssets("BarIcons/panel_brown.png",context);
        this.bitmap = BitmapManager.scale(this.bitmap, screenWidth,screenHeight-initY);
        this.bitmapButton = BitmapManager.getBitmapFromAssets("BarIcons/btn_not_pressed.png",context);
        this.bitmapButton = BitmapManager.scale(this.bitmapButton, (int)(boxSizeX),(int)(boxSizeY));
        this.bitmapButtonPressed = BitmapManager.getBitmapFromAssets("BarIcons/btn_pressed.png",context);
        this.bitmapButtonPressed = BitmapManager.scale(this.bitmapButtonPressed, (int)(boxSizeX),(int)(boxSizeY));
    }

    /**
     * The background of the DrawActionsBar
     * @param c canvas
     */
    public void draw(Canvas c){
        if(c != null) {
            c.drawBitmap(bitmap, 0, initY, null);
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
}
