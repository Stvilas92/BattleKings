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

    public void draw(Canvas c){
        if(c != null) {
            c.drawBitmap(bitmap, 0, initY, null);
        }
    }

    public int getInitY() {
        return initY;
    }

    public int getScreenHeight() {
        return screenHeight;
    }


    public Bitmap getBitmapButton() {
        return bitmapButton;
    }

    public Bitmap getBitmapButtonPressed() {
        return bitmapButtonPressed;
    }
}
