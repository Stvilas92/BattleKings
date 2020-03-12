package com.example.battlekings.DrawObjects.buildings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.battlekings.DrawObjects.DrawObjectSubtype;
import com.example.battlekings.DrawObjects.DrawObjectType;
import com.example.battlekings.DrawObjects.GameObject;
import com.example.battlekings.DrawObjects.OnTouchBarObjectResult;
import com.example.battlekings.DrawObjects.gameBars.DrawActionsBar;
import com.example.battlekings.DrawObjects.gameBars.DrawResourcesBar;
import com.example.battlekings.DrawObjects.humans.Human;
import com.example.battlekings.DrawObjects.humans.HumanOrientation;
import com.example.battlekings.DrawObjects.humans.HumanType;
import com.example.battlekings.Screen.Box;
import com.example.battlekings.Utils.BitmapManager;
import com.example.battlekings.Utils.GameTools;

import java.io.IOException;
import java.io.InputStream;

public class Building implements GameObject {
    private static final double RECT_HEIGTH = 1.5;
    private static final double RECT_WIDTH = 1.5;
    private static final int INIT_X = 2;
    private static final int SEPARATE = 2;
    private static final int RECTS_NUMBER_BUILDING = 5;
    private static final int RECTS_NUMBER_TOWER = 2;
    private static final int RECTS_NUMBER_WALL = 2;
    private static final int RECTS_NUMBER_CATAPULT = 2;
    private static final int INIT_LIFE = 200;
    private static final int SOLDIER_FOOD_COST = 50;
    private static final int CONSTRUCTOR_FOOD_COST = 100;
    private static final int VILLAGER_FOOD_COST = 30;

    private int sizeX = -1,sizeRectX;
    private int sizeY = -1,sizeRectY;
    private int[] boxesOcuped;
    private Box[] boxes;
    private int id,initBox,rectHeigth;
    private Bitmap buildBitmap,bitmapVillager,bitmapConstructor,bitmapSoldier;
    private Context context;
    private BuildingState state = BuildingState.STOPPED;
    private boolean selected = false;
    private DrawActionsBar drawActionsBar;
    private Paint p,pText;
    private Canvas c;

    //Game variables
    private BuildingType buildingType;
    private Rect[] rectActions;
    private int actualLife = INIT_LIFE;
    private BuildingState buildingState = BuildingState.STOPPED;
    private Runnable[] actions;
    private DrawResourcesBar drawResourcesBar;
    private boolean selectingMode = false;
    private BitmapManager bitmapManager;

    public Building(Box[] boxes, int id, int initBox, Context context, BuildingType buildingType, DrawActionsBar drawActionsBar, DrawResourcesBar drawResourcesBar, BitmapManager bitmapManager) {
        this.bitmapManager = bitmapManager;
        this.boxes = boxes;
        this.id = id;
        this.initBox = initBox;
        this.context = context;
        this.buildingType = buildingType;
        this.drawResourcesBar = drawResourcesBar;
        makeObjectToDraw();
        this.drawActionsBar = drawActionsBar;
        this.sizeRectY = (int)(boxes[0].getSizeY()*RECT_HEIGTH);
        this.sizeRectX = (int)(boxes[0].getSizeX()*RECT_WIDTH);
        rectHeigth = drawActionsBar.getInitY()+(((drawActionsBar.getScreenHeight()- drawActionsBar.getInitY()) - this.sizeRectY)/2);
        makeRectActions();
        this.p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        this.pText = new Paint();
        pText.setColor(Color.YELLOW);
        pText.setStyle(Paint.Style.STROKE);
        pText.setTextSize(boxes[0].getSizeY()/2);
    }

    /**
     * Draw the build object
     * @param c Canvas
     * @param x Point x
     * @param y Point x
     */
    @Override
    public void drawObject(Canvas c, int x, int y) {
        this.c = c;
        if(c != null) {
            c.drawBitmap(buildBitmap, x, y, null);
        }
    }

    /**
     * Draw the action bar buttons of the build object
     * @param c Canvas
     */
    @Override
    public void drawInActionBar(Canvas c) {
        for (int i = 0; i < rectActions.length; i++) {
            c.drawRect(rectActions[i],p);
            c.drawBitmap(drawActionsBar.getBitmapButton(),rectActions[i].left,rectActions[i].top,null);
        }

        switch(buildingType){
            case MAIN:
                c.drawBitmap(bitmapVillager,rectActions[0].left,rectActions[0].top,null);
                c.drawBitmap(bitmapConstructor,rectActions[1].left,rectActions[1].top,null);
                c.drawBitmap(bitmapSoldier,rectActions[2].left,rectActions[2].top,null);
                c.drawText(""+actualLife+"/"+INIT_LIFE,rectActions[3].left,rectActions[3].top+pText.getTextSize(),pText);
                c.drawText(buildingState.toString(),rectActions[4].left,rectActions[4].top+pText.getTextSize(),pText);
                break;

//            case WALL:
//                break;

            case TOWER:
                break;

//            case CATAPULT:
//                break;
        }
    }

    /**
     * Get the object id
     * @return object id
     */
    @Override
    public int getObjectID() {
        return this.id;
    }

    /**
     * Get the object bitmap
     * @return object bitmap
     */
    @Override
    public Bitmap getBitmap() {
        if(this.buildBitmap != null) {
            return this.buildBitmap;
        }else {
            return null;
        }
    }

    /**
     * Get boxes to draw the actual build object
     */
    private void getBoxesToDraw(){
        boxesOcuped = new int[sizeX*sizeY];
        int index = 0 ;

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if(i == 0 && j == 0){
                    boxesOcuped[index] = initBox;
                }else{
                    //TODO : Esto cogia las boxes de escenario, puede dar errores
                    boxesOcuped[index] = GameTools.getBoxByIndex(boxes,boxes[initBox].getIndexX()+i,boxes[initBox].getIndexX()+j);
                }
                index++;
            }
        }
    }

    /**
     * Do the action of the buttons of the action bar, if the buttons have actions.
     * @param x Point x to localize the button
     * @param y Point y to localize the button
     * @return OnTouchBarObjectResult, DROP_ALL_SELECTED or NONE
     */
    public OnTouchBarObjectResult onTouchActionBarObject(int x, int y){
        for (int i = 0; i < rectActions.length; i++) {
            if(rectActions[i].contains(x,y)){
                c.drawBitmap(drawActionsBar.getBitmapButtonPressed(),rectActions[i].left,rectActions[i].top,null);
                if(i == rectActions.length-1){
                    this.selectingMode = false;
                    this.selected = false;
                    return OnTouchBarObjectResult.DROP_ALL_SELECTED;
                }else {
                    actions[i].run();
                    return OnTouchBarObjectResult.NONE;
                }
            }
        }
        return OnTouchBarObjectResult.NONE;
    }



    /**
     * Charge the bitmaps to draw and the boxes witch will be occuped by him.
     * Put the object on boxes array and charge the value init resources
     */
    private void makeObjectToDraw(){
        switch (buildingType) {
            case MAIN:
                this.buildBitmap = bitmapManager.getBuildMain();
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.MAIN_BUILDING,this);
                setUnitsBitmaps();


                break;

            case TOWER:
                this.buildBitmap = bitmapManager.getBuildTower();
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.TOWER,this);
                break;

//            case WALL:
//                this.sizeX = 1;
//                this.sizeY = 1;
//                this.buildBitmap = getBitmapFromAssets("Buildings/wall.png");
//                this.buildBitmap = scaleByHeight(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
//                getBoxesToDraw();
//                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.WALL,this);
//                break;
//
//            case CATAPULT:
//                this.sizeX = 1;
//                this.sizeY = 1;
//                this.buildBitmap = getBitmapFromAssets("Buildings/catapult.png");
//                this.buildBitmap = scaleByHeight(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
//                getBoxesToDraw();
//                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.CATAPULT,this);
//                break;
        }
    }

    /**
     * Make the actions of the drawActionsBar buttons
     */
    private void makeRectActions(){
        switch(buildingType){
            case MAIN:
                rectActions = new Rect[RECTS_NUMBER_BUILDING];
                actions = new Runnable[RECTS_NUMBER_BUILDING-2];
                for (int i = 0; i < actions.length; i++) {
                    final int index = i;
                    actions[i] = (() -> setBuildingState(BuildingState.values()[index]));
                }
                break;

//            case WALL:
//                rectActions = new Rect[RECTS_NUMBER_WALL];
//                break;

            case TOWER:
                rectActions = new Rect[RECTS_NUMBER_TOWER];
                break;

//            case CATAPULT:
//                rectActions = new Rect[RECTS_NUMBER_CATAPULT];
//                break;
        }

        for (int i = 0; i < rectActions.length; i++) {
            rectActions[i] = new Rect(INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i),rectHeigth,INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i)+sizeRectX,rectHeigth+sizeRectY);
        }
    }

    /**
     * Get if building is selected
     * @return
     */
    @Override
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
     * Set if building is selected
     * @return selected, yes -true, no - false
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Set bitmap of the units, that will be drawed on the drawActionsBar
     */
    public void setUnitsBitmaps(){
        this.bitmapConstructor = bitmapManager.getBitmapConstructor();
        this.bitmapSoldier = bitmapManager.getBitmapSoldier();
        this.bitmapVillager = bitmapManager.getBitmapVillager();
    }

    /**
     * Set the actual building state
     * @param buildingState actual building state
     */
    public void setBuildingState(BuildingState buildingState) {
        this.buildingState = buildingState;

        switch (this.buildingState){
            case STOPPED:
                break;
            case ATTACKING:
                break;
            case DEFENDING:
                break;
            case CREATING_SOLDIER:
                if(drawResourcesBar.getActualFood() >= SOLDIER_FOOD_COST) {
                    Human soldier = new Human(boxes, 1, initBox - 1, context, HumanType.SOLDIER, drawActionsBar, HumanOrientation.SOUTH, drawResourcesBar,bitmapManager);
                    this.setBuildingState(BuildingState.STOPPED);
                    drawResourcesBar.setActualFood(drawResourcesBar.getActualFood()-SOLDIER_FOOD_COST);
                }
                break;
            case CREATING_VILLAGER:
                if(drawResourcesBar.getActualFood() >= VILLAGER_FOOD_COST) {
                    Human villager = new Human(boxes, 1, initBox - 1, context, HumanType.VILLAGER, drawActionsBar, HumanOrientation.SOUTH, drawResourcesBar,bitmapManager);
                    this.setBuildingState(BuildingState.STOPPED);
                    drawResourcesBar.setActualFood(drawResourcesBar.getActualFood()-VILLAGER_FOOD_COST);
                }
                break;
            case CREATING_CONSTRUCOR:
                if(drawResourcesBar.getActualFood() >= CONSTRUCTOR_FOOD_COST) {
                    Human constructor = new Human(boxes, 1, initBox - 1, context, HumanType.CONSTRUCTOR, drawActionsBar, HumanOrientation.SOUTH, drawResourcesBar,bitmapManager);
                    this.setBuildingState(BuildingState.STOPPED);
                    drawResourcesBar.setActualFood(drawResourcesBar.getActualFood()-CONSTRUCTOR_FOOD_COST);
                }
                break;
        }
    }

    /**
     * Manage a touch when the build is selected
     * @param boxIndex box touched when the build is selected
     * @return  box touched when the build is selected
     */
    @Override
    public int onTouchWhenSelected(int boxIndex) {
        return boxIndex;
    }

    /**
     * Get if the build is in selecting mode
     * @return selecting mode of the build
     */
    @Override
    public boolean isSelectingMode() {
        return selectingMode;
    }

    /**
     * Set if the build is in selecting mode
     * @param selectingMode selecting mode of the build
     */
    @Override
    public void setSelectingMode(boolean selectingMode) {
        this.selectingMode =  selectingMode;

    }

    /**
     * Manage the object when is touched
     * @param selecttingMode is the object on selecting mode or no
     * @param x point x
     * @param y point y
     * @param boxSelected actual box selected
     */
    @Override
    public void onTouchObject(boolean selecttingMode, int x,int y,int boxSelected) {
        if(!isSelected()){
            if(!selectingMode) {
                GameTools.deselectedAll(boxes);
                selected = true;
            }
        }else{
            if(y >= drawActionsBar.getInitY()){
                onTouchActionBarObject(x,y);
            }
        }
    }

    /**
     * Get the actual life of the building
     * @return actual life of the building
     */
    public int getActualLife() {
        return actualLife;
    }

    /**
     * Set the actual life of the building
     * @param actualLife actual life of the building
     */
    public void setActualLife(int actualLife) {
        this.actualLife = actualLife;
    }

    /**
     * Get the build type. Can be MAIN or TOWER
     * @return
     */
    public BuildingType getBuildingType() {
        return buildingType;
    }

    /**
     * Set the build type. Can be MAIN or TOWER
     * @param buildingType build type. Can be MAIN or TOWER
     */
    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
}
