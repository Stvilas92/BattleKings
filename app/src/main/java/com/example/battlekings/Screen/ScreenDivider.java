package com.example.battlekings.Screen;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.battlekings.Utils.BitmapManager;

/**
 * Class witch is used to divide all the surface to draw on boxes
 * with a specified size x (surface width) and y (surface height).
 */
public class ScreenDivider {
    /**
     * w width of the screen
     * h height of the screen
     * boxSizeX width of a box
     * boxSizeY height of a box
     */
    private int w,h,boxSizeX,boxSizeY;

    /** Array of boxes that will contains all the objects that will be draw*/
    private Box[] boxes;

    public ScreenDivider(int initX, int finalX, int initY, int finalY, int div, Context context ) {
        this.w = finalX-initX;
        this.h = finalY-initY;
        this.boxSizeX = w/div;
        this.boxSizeY = h/div;
        Bitmap floor = BitmapManager.getOnlySurface(boxSizeX,boxSizeY,context);
        boxes = new Box[((div)*(div))];

        int index= 0;
        for (int i = 0; i < div; i++) {
            for (int j = 0; j < div; j++) {
                boxes[index] = new Box(i,j,initX+(i*this.boxSizeX),initY+(j*this.boxSizeY),boxSizeX,boxSizeY,context,null,null,true,floor);
                index++;
            }
        }
    }

    /**
     * Get array of boxes called 'boxes'
     * @return boxes
     */
    public Box[] getBoxes() {
        return boxes;
    }
}
