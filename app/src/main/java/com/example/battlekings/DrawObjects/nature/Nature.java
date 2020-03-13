package com.example.battlekings.DrawObjects.nature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.battlekings.DrawObjects.DrawObjectSubtype;
import com.example.battlekings.DrawObjects.DrawObjectType;
import com.example.battlekings.DrawObjects.GameObject;
import com.example.battlekings.DrawObjects.OnTouchBarObjectResult;
import com.example.battlekings.DrawObjects.gameBars.DrawActionsBar;
import com.example.battlekings.Screen.Box;
import com.example.battlekings.Utils.BitmapManager;
import com.example.battlekings.Utils.GameTools;

public class Nature implements GameObject {
    private static final double RECT_HEIGTH = 1.5;
    private static final double RECT_WIDTH = 1;
    private static final int INIT_X = 2;
    private static final int SEPARATE = 2;
    private static final int RECTS_NUMBER = 2;
    private static final int INIT_ROCK = 50;
    private static final int INIT_FOOD = 150;
    private static final int INIT_WOOD = 50;

    private int sizeX = -1;
    private int sizeY = -1;
    private int[] boxesOcuped;
    private Box[] boxes;
    private int id, actualBox,rectHeigth,sizeRectY,sizeRectX;
    private Bitmap natureBitmap,natureTypeBitmap;
    private Context context;
    private boolean selected = false;
    private Rect[] rectActions;
    private Paint p,pText;
    private BitmapManager bitmapManager;

    //Game Data
    private NatureType natureType;
    private NatureState natureState = NatureState.STTOPED;
    int initResources,actualResources;

    public Nature(Box[] boxes, int id, int actualBox, Context context, NatureType natureType, DrawActionsBar drawActionsBar, BitmapManager bitmapManager) {
        this.bitmapManager = bitmapManager;
        this.boxes = boxes;
        this.id = id;
        this.actualBox = actualBox;
        this.context = context;
        this.natureType = natureType;
        this.sizeRectY = (int)(boxes[0].getSizeY()*RECT_HEIGTH);
        this.sizeRectX = (int)(boxes[0].getSizeX()*RECT_WIDTH);
        rectHeigth = drawActionsBar.getInitY()+(((drawActionsBar.getScreenHeight()- drawActionsBar.getInitY()) - this.sizeRectY)/2);
        makeObjectToDraw();
        makeRectActions();
        this.p = new Paint();
        p.setColor(Color.YELLOW);
        p.setStyle(Paint.Style.STROKE);
        this.pText = new Paint();
        pText.setColor(Color.YELLOW);
        pText.setStyle(Paint.Style.FILL_AND_STROKE);
        pText.setTextSize(boxes[0].getSizeY()/2);
    }

    /**
     * Draw the nature object
     * @param c Canvas
     * @param x Point x
     * @param y Point x
     */
    @Override
    public void drawObject(Canvas c, int x, int y) {
        if(c != null) {
            c.drawBitmap(natureBitmap, x, y, null);
        }
    }

    /**
     * Draw the action bar buttons of the nature object
     * @param c Canvas
     */
    @Override
    public void drawInActionBar(Canvas c) {
        for (int i = 0; i < rectActions.length; i++) {
            c.drawRect(rectActions[i],p);
            if( i == 0){
                c.drawBitmap(natureTypeBitmap,rectActions[i].left,rectActions[i].top,null);
            }else if( i == 1){
                c.drawText(""+actualResources+"/"+initResources,rectActions[i].left,rectActions[i].top+(boxes[0].getSizeY()/2),pText);
            }
        }
    }

    /**
     * Do the action of the buttons of the action bar, if the buttons have actions.
     * @param x Point x to localize the button
     * @param y Point y to localize the button
     * @return OnTouchBarObjectResult, in this case, always return NONE
     */
    @Override
    public OnTouchBarObjectResult onTouchActionBarObject(int x, int y) {
        return OnTouchBarObjectResult.NONE;
    }


    /**
     * Get the object bitmap
     * @return object bitmap
     */
    @Override
    public Bitmap getBitmap() {
        if(this.natureBitmap != null) {
            return this.natureBitmap;
        }else {
            return null;
        }
    }

    /**
     * Get boxes to draw the actual nature object
     */
    private void getBoxesToDraw(){
        boxesOcuped = new int[sizeX*sizeY];
        int index = 0 ;

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if(i == 0 && j == 0){
                    boxesOcuped[index] = actualBox;
                }else{
                    //TODO: aqui tambien se llamaba desde escenario
                    boxesOcuped[index] = GameTools.getBoxByIndex(boxes,boxes[actualBox].getIndexX()+i,boxes[actualBox].getIndexX()+j);
                }
                index++;
            }
        }
    }

    /**
     * Charge the bitmaps to draw and the boxes witch will be occuped by him.
     * Put the object on boxes array and charge the value init resources
     */
    private void makeObjectToDraw(){
        switch (natureType) {
            case ROCK:
                this.natureBitmap = bitmapManager.getNatureRock();
                this.natureTypeBitmap =  bitmapManager.getNatureTypeRock();
                getBoxesToDraw();
                this.boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING, DrawObjectSubtype.MAIN_BUILDING,this);
                this.initResources =  INIT_ROCK;
                break;

            case FOOD:
                this.natureBitmap = bitmapManager.getNatureFood();
                this.natureTypeBitmap =  bitmapManager.getNatureTypeFood();
                getBoxesToDraw();
                this.boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING, DrawObjectSubtype.TOWER,this);
                this.initResources =  INIT_FOOD;
                break;

            case WOOD:
                this.natureBitmap = bitmapManager.getNatureWood();
                this.natureTypeBitmap =  bitmapManager.getNatureTypeWood();
                getBoxesToDraw();
                this.boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING, DrawObjectSubtype.WALL,this);
                this.initResources =  INIT_WOOD;
                break;
        }
        this.actualResources = this.initResources;
    }

    /**
     * Make the actions of the drawActionsBar buttons
     */
    private void makeRectActions(){
        rectActions = new Rect[RECTS_NUMBER];

        for (int i = 0; i < rectActions.length; i++) {
            rectActions[i] = new Rect(INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i),rectHeigth,INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i)+sizeRectX,rectHeigth+sizeRectY);
        }
    }

    /**
     * Get if building is selected
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Get the size X of the nature object
     * @return size X of the nature object
     */
    @Override
    public int getSizeX() {
        return this.sizeX;
    }

    /**
     * Get the size Y of the nature object
     * @return size Y of the nature object
     */
    @Override
    public int getSizeY() {
        return this.sizeY;
    }

    /**
     * Set if nature is selected
     * @return selected, yes -true, no - false
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Manage a touch when the nature is selected
     * @param boxIndex box touched when the nature is selected
     * @return  box touched when the nature is selected
     */
    @Override
    public int  onTouchWhenSelected(int boxIndex) {
        return boxIndex;
    }

    /**
     * Get if the nature is in selecting mode
     * @return if the nature is in selecting mode
     */
    @Override
    public boolean isSelectingMode() {
        return false;
    }

    /**
     * Set if the nature is in selecting mode
     * @param selectingMode selecting mode of the nature
     */
    @Override
    public void setSelectingMode(boolean selectingMode) {
    }

    /**
     * Manage the object when is touched
     * @param selectingMode is the object on selecting mode or no
     * @param x point x
     * @param y point y
     * @param boxSelected actual box selected
     */
    @Override
    public void onTouchObject(boolean selectingMode, int x,int y,int boxSelected) {
        if(!isSelected() && !selectingMode){
            GameTools.deselectedAll(boxes);
            selected = true;
        }
    }

    /**
     * Get the actual resources of the nature
     * @return actual resources of the nature
     */
    public int getActualResources() {
        return actualResources;
    }

    /**
     * Set the actual resources of the nature
     * @param actualResources actual resources of the nature
     */
    public void setActualResources(int actualResources) {
        this.actualResources = actualResources;
        if(this.actualResources <= 0){
            boxes[actualBox].setDrawObjectTypeAndSubtype(null,null,null);
        }
    }

    /**
     * Get the actual nature type
     * @return actual nature type
     */
    public NatureType getNatureType() {
        return natureType;
    }

    /**
     * Get the actual box of the human.
     *
     * @return actual box of the human.
     */
    @Override
    public int getActualBox() {
        return actualBox;
    }
}
