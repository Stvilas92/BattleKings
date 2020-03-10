package com.example.battlekings.DrawObjects.gameBars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

public class DrawResourcesBar {
    private static final int INIT_RECT_INDEX_FOOD = 1;
    private static final int INIT_RECT_INDEX_WOOD = 3;
    private static final int INIT_RECT_INDEX_STONE = 5;
    private static final int RECT_WITDH = 2;

    private Bitmap foodBitmap,woodBitmap,stoneBitmap;
    private int actualWood,actualStone,actualFood;
    private int initY,boxSizeX,boxSizeY,screenWidth;
    private Paint pText,pResources;
    private Rect rBackgroud,rFood,rWood,rStone;
    private Context context;

    public DrawResourcesBar(int actualWood, int actualStone, int actualFood, int initY, int boxSizeX, int boxSizeY, int screenWidth, Context context) {
        this.actualWood = actualWood;
        this.actualStone = actualStone;
        this.actualFood = actualFood;
        this.initY = initY;
        this.boxSizeX = boxSizeX;
        this.boxSizeY = boxSizeY;
        this.screenWidth = screenWidth;
        this.context = context;
        this.pText = new Paint();
        this.pText.setColor(Color.YELLOW);
        this.pText.setTextSize(boxSizeY/2);
        this.pResources = new Paint();
        this.pResources.setColor(Color.BLACK);
        this.rBackgroud = new Rect(0,0,screenWidth,initY);
        this.rFood = getRect(INIT_RECT_INDEX_FOOD);
        this.rWood = getRect(INIT_RECT_INDEX_WOOD);
        this.rStone = getRect(INIT_RECT_INDEX_STONE);
        this.foodBitmap = getBitmap("Resources/food.png");
        this.stoneBitmap = getBitmap("Resources/stone.png");
        this.woodBitmap = getBitmap("Resources/wood.png");
    }

    /**
     * Draw resources bar and rectangles
     * @param c
     */
    public void draw(Canvas c){
        if(c != null) {
            c.drawRect(rStone, pResources);
            c.drawRect(rFood, pResources);
            c.drawRect(rWood, pResources);
            c.drawBitmap(foodBitmap, (boxSizeX * INIT_RECT_INDEX_FOOD), 0, null);
            c.drawBitmap(woodBitmap, (boxSizeX * INIT_RECT_INDEX_WOOD), 0, null);
            c.drawBitmap(stoneBitmap, (boxSizeX * INIT_RECT_INDEX_STONE), 0, null);
            c.drawText("" + actualFood, boxSizeX * (INIT_RECT_INDEX_FOOD + 1), initY / 2, pText);
            c.drawText("" + actualStone, boxSizeX * (INIT_RECT_INDEX_STONE + 1), initY / 2, pText);
            c.drawText("" + actualWood, boxSizeX * (INIT_RECT_INDEX_WOOD + 1), initY / 2, pText);
        }
    }

    /**
     * Get a rectangle by index cont since the left side
     * @param indexLeft
     * @return
     */
    private Rect getRect(int indexLeft){
        Rect r = new Rect();
        r = new Rect(boxSizeX*indexLeft,0,(boxSizeX*indexLeft)+(boxSizeX*RECT_WITDH),initY);
        return r;
    }

    /**
     * Get a bitmap scaled by a box size height
     * @param bitmap bitmap to be sacled
     * @return bitmap sacled
     */
    private Bitmap getBitmap(String bitmap){
        Bitmap b = getBitmapFromAssets(bitmap);
        return scaleByHeight(b,boxSizeY);
    }

    /**
     * Get a bitmap from a assets folder
     * @param file file of the assets folder to convert on bitmap
     * @return bitmap charge from file of the assets folder
     */
    public Bitmap getBitmapFromAssets(String file) {
        try
        {
            InputStream is= context.getAssets().open(file);
            return BitmapFactory.decodeStream(is);
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Scale a bitmap by height
     * @param res Bitmap to sacale
     * @param newHeight new height of the bitmap
     * @return bitmap sacaled
     */
    public Bitmap scaleByHeight(Bitmap res, int newHeight) {
        if (newHeight==res.getHeight()) return res;
        return res.createScaledBitmap(res, (res.getWidth() * newHeight) /
                res.getHeight(), newHeight, true);
    }

    /**
     * Get actual wood of the player
     * @return actual wood of the player
     */
    public int getActualWood() {
        return actualWood;
    }

    /**
     * Set actual wood of the player
     * @param actualWood actual wood of the player
     */
    public void setActualWood(int actualWood) {
        this.actualWood = actualWood;
    }

    /**
     * Get actual stone of the player
     * @return actual stone of the player
     */
    public int getActualStone() {
        return actualStone;
    }

    /**
     * Set actual stone of the player
     * @param actualStone actual stone of the player
     */
    public void setActualStone(int actualStone) {
        this.actualStone = actualStone;
    }

    /**
     * Get actual food of the player
     * @return actual food of the player
     */
    public int getActualFood() {
        return actualFood;
    }

    /**
     * Set actual food of the player
     * @param actualFood actual food of the player
     */
    public void setActualFood(int actualFood) {
        this.actualFood = actualFood;
    }
}
