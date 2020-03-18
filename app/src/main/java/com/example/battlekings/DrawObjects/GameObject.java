package com.example.battlekings.DrawObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Use to make actions on a each objetc that will be draw on the screen.
 */
public interface GameObject {
    public void drawObject(Canvas c, int x, int y);
    public Bitmap getBitmap();
    public boolean isSelected();
    public int getSizeX();
    public int getSizeY();
    public void setSelected(boolean selected);
    public void drawInActionBar(Canvas c);
    public OnTouchBarObjectResult onTouchActionBarObject(int x, int y);
    public int onTouchWhenSelected(int boxIndex);
    public boolean isSelectingMode();
    public void setSelectingMode(boolean selectingMode);
    public void onTouchObject(boolean selectingMode, int x, int y, int boxSelected);
    public int getActualBox();
}
