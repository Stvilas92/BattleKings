package com.example.battlekings.Screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.example.battlekings.DrawObjects.DrawObjectSubtype;
import com.example.battlekings.DrawObjects.DrawObjectType;
import com.example.battlekings.DrawObjects.GameObject;

/**
 * Represents a box. Is a division of the screen as a rectangle.
 * Contains a surface to draw always on the game and might contains
 * a GameObject that will be draw.
 * The game object can be drawed in different points of the (x,y) into the box.
 */
public class Box {
    /** Number max of boxes horizonal or vertial*/
    public static final int DIV = 20;
    /** Point x into the box witch the game object are draw*/
    public static final int MOVING_X_SIZE = 8;
    /** Point y into the box witch the game object are draw*/
    public static final int MOVING_Y_SIZE = 4;

    /**
     * xReferences references a other x index of another box.
     * yReference references a other y index of another box.
     * xReferenceCoord references a other x coordinate of another box.
     * yReferenceCoord references a other y coordinate of another box.
     */
    int xReference,yReference;
    int xReferenceCoord,yReferenceCoord;

    /**
     * x point x of the screen to init draw
     * y point y of the screen to init draw
     * finalX  x of the screen to finish draw
     * finalY  y of the screen to finish draw
     * indexX index x of the box on a boxes array
     * indexY index y of the box on a boxes array
     * sizeX width of the box
     * sizeY height of the box
     */
    private int x,y,finalX,finalY,indexX,indexY,sizeX,sizeY;
    /**
     * Bitmaps to draw
     * floor bitmap of the surface
     * object bitmap of the object
     */
    private Bitmap floor,object;
    /** Indicates if an box is interactable*/
    private boolean interactable;
    /** Application context*/
    private Context context;
    /** DrawObjectType of the box. Can be null*/
    private DrawObjectType drawObjectType;
    /** DrawObjectSubtype of the box. Can be null*/
    private DrawObjectSubtype drawObjectSubtype;
    /** GameObject of the box. Can be null*/
    private GameObject gameObject;
    /**
     * Points into the box where the GameObject will be draw
     * movingX points x
     * movingY points y
     */
    private int[] movingX,movingY;
    /**
     * actualGameObjectIndexX actual index of movingX
     * actualGameObjectIndexY actual index of movingY
     * middleIndexX middle index of movingX
     */
    private int actualGameObjectIndexX,actualGameObjectIndexY ,middleIndexX;
    /** Paint to draw on the box */
    private Paint p=new Paint();


    public Box(int indexX, int indexY, int x, int y, int sizeX, int sizeY, Context context, DrawObjectType drawObjectType, DrawObjectSubtype drawObjectSubtype, boolean interactable , Bitmap surface) {
        this.x = x;
        p.setColor(Color.YELLOW);
        p.setStrokeWidth(1);
        p.setStyle(Paint.Style.STROKE);
        p.setTextSize(sizeY/2);
        this.y = y;
        this.finalX = x+sizeX;
        this.finalY = y+sizeY;
        this.indexX = indexX;
        this.indexY = indexY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.context  = context;
        this.floor = surface;
        this.drawObjectType = drawObjectType;
        this.movingX = new int[MOVING_X_SIZE];
        this.movingY = new int[MOVING_Y_SIZE];

        this.middleIndexX = MOVING_X_SIZE/2;
        this.actualGameObjectIndexX = middleIndexX;
        this.actualGameObjectIndexY = 0;

        for (int i = 0; i < movingX.length; i++) {
            movingX[i] = x+(i*(sizeX/MOVING_X_SIZE));
        }

        for (int i = 0; i < movingY.length; i++) {
            movingY[i] = y+(i*(sizeY/MOVING_Y_SIZE));
        }
    }

    /**
     * Draw the box on the screen
     * @param c canvas
     */
    public void drawBox(Canvas c){
        if(drawObjectType != null && drawObjectSubtype != null){
            drawObject(c);
        }
    }

    /**
     * Draw the object of the box
     * @param c canvas
     */
    private void drawObject(Canvas c){
        if(c != null) {
            this.gameObject.drawObject(c, movingX[actualGameObjectIndexX], movingY[actualGameObjectIndexY]);

            if (gameObject != null && gameObject.isSelected() && drawObjectType != DrawObjectType.BUILDING) {
                c.drawRect(new Rect(this.movingX[actualGameObjectIndexX], movingY[this.actualGameObjectIndexY],
                        this.movingX[actualGameObjectIndexX] + (this.sizeX),  movingY[this.actualGameObjectIndexY] + (this.sizeY)), p);
            }
        }
    }

    /**
     * Draw the surface of the box
     * @param c canvas
     */
    public void drawFloor(Canvas c){
        if(floor != null && c != null) {
            c.drawBitmap(this.floor, this.x, this.y, null);
        }
    }

    /**
     * Get the DrawObjectType of the box
     * @return
     */
    public DrawObjectType getDrawObjectType() {
        return drawObjectType;
    }

    /**
     * Get the DrawObjectSubtype of the box
     * @return
     */
    public DrawObjectSubtype getDrawObjectSubtype() {
        return drawObjectSubtype;
    }

    public void setDrawObjectTypeAndSubtype(DrawObjectType drawObjectType, DrawObjectSubtype drawObjectSubtype, GameObject gameObject) {
        this.drawObjectType = drawObjectType;
        this.drawObjectSubtype = drawObjectSubtype;
        this.gameObject = gameObject;
        if(drawObjectType != null){
            if(gameObject == null) {
                this.object = null;
            }else{
                this.object = gameObject.getBitmap();
            }
        }
    }

    /**
     * Get the width of the box
     * @return width of the box
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * Get the height of the box
     * @return height of the box
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * Get index x of the box
     * @return x of the box
     */
    public int getIndexX() {
        return indexX;
    }

    /**
     * Get index y of the box
     * @return y of the box
     */
    public int getIndexY() {
        return indexY;
    }

    /**
     * Get  x of the box
     * @return x of the box
     */
    public int getX() {
        return x;
    }

    /**
     * Set x of the box
     * @param x x of the box
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get  y of the box
     * @return y of the box
     */
    public int getY() {
        return y;
    }

    /**
     * Set y of the box
     * @param y y of the box
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the final point x of the box
     * @return final point x of the box
     */
    public int getFinalX() {
        return finalX;
    }

    /**
     * Get the final point x of the box
     * @return final point x of the box
     */
    public int getFinalY() {
        return finalY;
    }

    /**
     * Get the bitmap of the surface
     * @return bitmap of the surface
     */
    public Bitmap getFloor() {
        return floor;
    }

    /**
     * Get the application context
     * @return application context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Set the application context
     * @param context application context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Get the game object of the box
     * @return game object of the box
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    /**
     * Set the game object of the box
     * @param gameObject game object of the box
     */
    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    /**
     * Get the xReference of the box
     * @return xReference of the box
     */
    public int getxReference() {
        return xReference;
    }

    /**
     * Get the yReference of the box
     * @return yReference of the box
     */
    public int getyReference() {
        return yReference;
    }

    /**
     * Get the actualGameObjectIndexX of the box
     * @return actualGameObjectIndexX of the box
     */
    public int getActualGameObjectIndexX() {
        return actualGameObjectIndexX;
    }

    /**
     * Set the actualGameObjectIndexX of the box
     * @param actualGameObjectIndexX actualGameObjectIndexY of the box
     */
    public void setActualGameObjectIndexX(int actualGameObjectIndexX) {
        this.actualGameObjectIndexX = actualGameObjectIndexX;
    }

    /**
     * Get the actualGameObjectIndexY of the box
     * @return actualGameObjectIndexY of the box
     */
    public int getActualGameObjectIndexY() {
        return actualGameObjectIndexY;
    }

    /**
     * Set the actualGameObjectIndexY of the box
     * @param actualGameObjectIndexY actualGameObjectIndexY of the box
     */
    public void setActualGameObjectIndexY(int actualGameObjectIndexY) {
        this.actualGameObjectIndexY = actualGameObjectIndexY;
    }

    /**
     * Get the constant DIV
     * @return MOVING_X_SIZE
     */
    public static int getMovingXSize() {
        return MOVING_X_SIZE;
    }

    /**
     * Get the constant DIV
     * @return MOVING_Y_SIZE
     */
    public static int getMovingYSize() {
        return MOVING_Y_SIZE;
    }

    /**
     * Get the middleIndexX of the box
     * @return  middleIndexX of the box
     */
    public int getMiddleIndexX() {
        return middleIndexX;
    }

    /**
     * Get the constant DIV
     * @return DIV
     */
    public static int getDIV() {
        return DIV;
    }

    /**
     * Get if the box is interactable
     * @return  box is interactable or no
     */
    public boolean isInteractable() {
        return interactable;
    }
}
