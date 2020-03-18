package com.example.battlekings.DrawObjects.humans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import com.example.battlekings.DrawObjects.DrawObjectSubtype;
import com.example.battlekings.DrawObjects.DrawObjectType;
import com.example.battlekings.DrawObjects.GameObject;
import com.example.battlekings.DrawObjects.OnTouchBarObjectResult;
import com.example.battlekings.DrawObjects.buildings.Building;
import com.example.battlekings.DrawObjects.buildings.BuildingType;
import com.example.battlekings.DrawObjects.gameBars.DrawActionsBar;
import com.example.battlekings.DrawObjects.gameBars.DrawResourcesBar;
import com.example.battlekings.DrawObjects.nature.Nature;
import com.example.battlekings.GameManger.Escenario;
import com.example.battlekings.R;
import com.example.battlekings.Screen.Box;
import com.example.battlekings.Utils.BitmapManager;
import com.example.battlekings.Utils.GameTools;
import java.util.ArrayList;
import java.util.HashMap;
import androidx.core.content.res.ResourcesCompat;

/**
 * Represents a human. Human can be do different things depending on the type (VILLAGER,CONSTRUCTOR,SOLDIER)
 */
public class Human implements GameObject {
    /** Rect height of draw action bar buttons*/
    private static final double RECT_HEIGTH = 1.5;
    /** Rect width of draw action bar buttons*/
    private static final double RECT_WIDTH = 1.5;
    /** Init point x to draw action bar buttons*/
    private static final int INIT_X = 2;
    /** Distance between the buttons of the action bar*/
    private static final int SEPARATE = 2;
    /** Rects number of the action bar*/
    private static final int RECTS_NUMBER_HUMAN = 3;
    /** Init human life*/
    private static final int INIT_LIFE = 100;
    /** Points of the main build life that a constructor repair when he does an action*/
    private static final int REPAIR = 1;
    /** Points of a resource that a villager get when he does an action*/
    private static final int COLLECT = 1;
    /** Points of a enemy life down when a soldier  does an action*/
    private static final int ATACK = 10;
    /** Wood needed to repair one point of the main building*/
    private static final int WOOD_TO_REPAIR = 2;
    /** Stone needed to repair one point of the main building*/
    private static final int STONE_TO_CONSTRUCT = 2;

    /**
     * sizeX size of the box witch contains a human
     * sizeRectX size of the rect of drawActionBar
     */
    private int sizeX = 1, sizeRectX;

    /**
     * sizeY size of the box witch contains a human
     * sizeRectY size of the rect of drawActionBar
     */
    private int sizeY = 1, sizeRectY;
    /** Total boxes */
    private Box[] boxes;
    /**
     * actualBox Actual box occupied by the human
     * lastBox Last box occupied by the human
     * rectHeight Rect height of the drawActionBar
     */
    private int  actualBox, rectHeigth, lastBox;
    /**
     * humanBitmap bitmap of a human when the action is ON_STOPPED
     * bitmapVillager bitmap of a villager when the action is ON_STOPPED
     * bitmapConstructor constructor of a constructor when the action is ON_STOPPED
     * bitmapSoldier soldier of a soldier when the action is ON_STOPPED
     * exitBitmap bitmap of a exit button on th DrawActionsBar
     * actionBitmap bitmap of action button DrawActionsBar
     * bitmapEnemy bitmap of a enemy when the action is ON_STOPPED
     */
    private Bitmap humanBitmap, bitmapVillager, bitmapConstructor, bitmapSoldier, exitBitmap, actionBitmap, bitmapEnemy;

    /**
     * humanWalking human bitmaps related with actual human orientation to draw when the action is ON_WALKING
     * humanAction human bitmaps related with actual human orientation to draw when the action is ON_ACTION
     * humanDead human bitmaps related with actual human orientation to draw when the action is ON_DEAD
     */
    private HashMap<HumanOrientation, Bitmap[]> humanWalking, humanAction, humanDead;
    /** Application context*/
    private Context context;
    /** Indicate if the human is selected */
    private boolean selected = false;
    /** DrawActionsBar of the human */
    private DrawActionsBar drawActionsBar;
    /** DrawResourcesBar of the game */
    private DrawResourcesBar drawResourcesBar;
    /**
     * p paint to draw on the box
     * pText paint to draw on the DrawActionsBar
     */
    private Paint p, pText;
    /** DrawObjectSubtype of the box that contains a human*/
    private DrawObjectSubtype drawObjectSubtype;
    /** Canvas to draw */
    private Canvas c;

    //Game variables
    /** Actual human type*/
    private HumanType humanType;
    /** Actual human orientation*/
    private HumanOrientation humanOrientation;
    /** Rects of the action bar*/
    private Rect[] rectActions;
    /** Actions of each rect of the action bar*/
    private Runnable[] actions;
    /** Human init life*/
    private int actualLife = INIT_LIFE;
    /** Actual human state*/
    private HumanState humanState = HumanState.STTOPED;
    /**
     * boxDestiny box to move when the state is ON_ACTION or ON_WALKING
     * boxMiddleDestiny box alternative to dodge an object when the state is ON_ACTION or ON_WALKING
     */
    private int boxDestiny = -1,boxMiddleDestiny = -1;
    /**
     * movingDifferenceX horizontal difference between boxDestiny and actualBox
     * movingDifferenceY vertical difference between boxDestiny and actualBox
     */
    private int movingDifferenceX, movingDifferenceY;
    /** Indexes to select a bitmap on humanWalking, humanAction, humanDead*/
    private int walkingIndex = 0, actionIndex = 0, deadIndex = 0;
    /** Indicates the end of the action*/
    private boolean flagActionEnd = false;
    /** Indicates if the user is selecting a new objectObjetive to do an action*/
    private boolean selecttingMode = false;
    /** GameObject objetive when the human is on ON_ACTION state*/
    private GameObject objectObjetive;
    /** Bitmap Manager of the game*/
    private BitmapManager bitmapManager;
    /** Indicate if a bitmap is end to draw*/
    private boolean flagEndBitmap = false;
    private  int lastIndexChangued = 0,contMoveDirection = 0;
    /** Last diference of the boxes left until the box destiny */
    private String lastDifference = "";

    private ArrayList<Integer>lastIndexesWalked = new ArrayList<>();

    public Human(Box[] boxes, int id, int actualBox, Context context, HumanType humanType, DrawActionsBar drawActionsBar, HumanOrientation humanOrientation, DrawResourcesBar drawResourcesBar, BitmapManager bitmapManager) {
        this.bitmapManager = bitmapManager;
        this.boxes = boxes;
        this.drawResourcesBar = drawResourcesBar;
        this.humanOrientation = humanOrientation;
        this.actualBox = actualBox;
        this.lastBox = -1;
        this.context = context;
        this.humanType = humanType;
        makeObjectToDraw();
        this.drawActionsBar = drawActionsBar;
        this.sizeRectY = (int) (boxes[0].getSizeY() * RECT_HEIGTH);
        this.sizeRectX = (int) (boxes[0].getSizeX() * RECT_WIDTH);
//        rectHeigth = drawActionsBar.getInitY() + (((drawActionsBar.getScreenHeight() - drawActionsBar.getInitY()) - this.sizeRectY) / 2);
        rectHeigth =  drawActionsBar.getInitY();
        makeRectActions();
        this.p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        this.pText = new Paint();
        pText.setColor(Color.YELLOW);
        pText.setStyle(Paint.Style.STROKE);
        pText.setTextSize(boxes[0].getSizeY() / 2);
        Typeface ttf = ResourcesCompat.getFont(context, R.font.prince_valiant);
        pText.setTypeface(ttf);
        boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, drawObjectSubtype, this);
    }

    /**
     * Draw the human in a canvas in the xy selected position. The human draw will depend on the human state
     *
     * @param c Canvas where the human will be draw.
     * @param x x selected position
     * @param y y selected position
     */
    @Override
    public void drawObject(Canvas c, int x, int y) {
        this.c = c;
        if (c != null) {
            switch (humanState) {
                case STTOPED:
                    c.drawBitmap(humanBitmap, x, y, null);
                    break;

                case WALKING:
                    if (boxDestiny >= 0) {
                        setMovementDirection();
                        c.drawBitmap(humanWalking.get(humanOrientation)[walkingIndex], x, y, null);
                        walkingIndex++;
                        if (walkingIndex >= humanWalking.values().size()) {
                            walkingIndex = 0;
                        }
                    }else {
                        humanState = HumanState.STTOPED;
                        c.drawBitmap(humanBitmap, x, y, null);
                    }
                    break;


                case ONACTION:
                    if (boxDestiny >= 0) {
                        if(humanType == HumanType.ENEMY || humanType == HumanType.CONSTRUCTOR ) {
                            if(actualBox != boxDestiny) {
                                setEnemyOrConstructorDestiny();
                            }
                        }else{
                            changeSoldierOrCollectorDestiny();
                        }
                        setMovementDirection();
                        c.drawBitmap(humanWalking.get(humanOrientation)[walkingIndex], x, y, null);
                        walkingIndex++;
                        if (walkingIndex >= humanWalking.values().size()) {
                            walkingIndex = 0;
                        }
                    } else {
                        setOrientationAction();
                        if (flagActionEnd) {
                            humanState = HumanState.STTOPED;
                            selecttingMode = false;
                            flagActionEnd = false;
                        } else {
                            c.drawBitmap(humanAction.get(humanOrientation)[actionIndex], x, y, null);
                            if (!flagEndBitmap) {
                                actionIndex++;
                                if (actionIndex >= humanAction.get(HumanOrientation.EST).length) {
                                    actionIndex--;
                                    flagEndBitmap = true;
                                }
                            } else {
                                actionIndex--;
                                if (actionIndex == 0) {
                                    actionIndex++;
                                    doAction(objectObjetive);
                                    flagEndBitmap = false;
                                }
                            }
                        }
                    }
                    break;

                case DEAD:
                    c.drawBitmap(humanDead.get(humanOrientation)[deadIndex], x, y, null);
                    deadIndex++;
                    if (deadIndex >= humanDead.values().size()) {
                        deadIndex = 0;
                    }
                    boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                    break;
            }
        }
    }

    /**
     * Draw the rects and bitmaps action bar. Each human have a different action bar.
     *
     * @param c
     */
    @Override
    public void drawInActionBar(Canvas c) {
        for (int i = 0; i < rectActions.length; i++) {
            c.drawRect(rectActions[i], p);
            c.drawBitmap(drawActionsBar.getBitmapButton(), rectActions[i].left, rectActions[i].top, null);
        }

        c.drawBitmap(actionBitmap, rectActions[0].left, rectActions[0].top, null);
        c.drawText("" + actualLife + "/" + INIT_LIFE, rectActions[1].left, rectActions[1].top + pText.getTextSize(), pText);
        c.drawBitmap(exitBitmap, rectActions[2].left, rectActions[2].top, null);
    }

    /**
     * Run the action contained on the rect, pressed by the user.
     *
     * @param x X coordenate pressed by the user
     * @param y Y coordenate pressed by the user
     * @return Result of the action.
     */
    @Override
    public OnTouchBarObjectResult onTouchActionBarObject(int x, int y) {
        if (humanType == HumanType.ENEMY) {
            return OnTouchBarObjectResult.NONE;
        }

        for (int i = 0; i < rectActions.length; i++) {
            if (rectActions[i].contains(x, y)) {
                c.drawBitmap(drawActionsBar.getBitmapButtonPressed(), rectActions[i].left, rectActions[i].top, null);
                actions[i].run();

                if (i == rectActions.length - 1) {
                    return OnTouchBarObjectResult.DROP_ALL_SELECTED;
                } else {
                    return OnTouchBarObjectResult.NONE;
                }
            }
        }
        return OnTouchBarObjectResult.NONE;
    }

    /**
     * Get the general bitmap of the human
     *
     * @return humanBitmap
     */
    @Override
    public Bitmap getBitmap() {
        if (this.humanBitmap != null) {
            return this.humanBitmap;
        } else {
            return null;
        }
    }

    /**
     * Create the bitmap general to draw and createHuman to create the rest bitmaps and
     * put the DrawObject type and subtype on the actualBox.
     */
    private void makeObjectToDraw() {
        setUnitsBitmaps();
        switch (humanType) {
            case VILLAGER:
                this.humanBitmap = bitmapVillager;
                createHuman(actualBox, HumanType.VILLAGER);
                drawObjectSubtype = DrawObjectSubtype.VILLAGER;
                break;

            case SOLDIER:
                this.humanBitmap = bitmapSoldier;
                createHuman(actualBox, HumanType.SOLDIER);
                drawObjectSubtype = DrawObjectSubtype.SOLDIER;
                break;

            case CONSTRUCTOR:
                this.humanBitmap = bitmapConstructor;
                createHuman(actualBox, HumanType.CONSTRUCTOR);
                drawObjectSubtype = DrawObjectSubtype.CONSTRUCTOR;
                break;

            case ENEMY:
                this.humanBitmap = bitmapEnemy;
                createHuman(actualBox, HumanType.ENEMY);
                drawObjectSubtype = DrawObjectSubtype.ENEMY;
                break;
        }
    }

    /**
     * Create the Rects array using to draw the options of the DrawActionBar and
     * create the actions of each rect that it will be realized if the user touch on the Rects
     */
    private void makeRectActions() {
        rectActions = new Rect[RECTS_NUMBER_HUMAN];
        actions = new Runnable[RECTS_NUMBER_HUMAN];

        if (humanType != HumanType.ENEMY) {
            actions[0] = (() -> {
                setSelecttingMode(!selecttingMode);
            });
            actions[1] = (() -> {
                return;
            });
            actions[2] = (() -> {
                setSelected(false);
                setSelecttingMode(false);
            });
        }
        for (int i = 0; i < rectActions.length; i++) {
            rectActions[i] = new Rect(INIT_X * boxes[0].getSizeX() + ((SEPARATE * boxes[0].getSizeX()) * i), rectHeigth, INIT_X * boxes[0].getSizeX() + ((SEPARATE * boxes[0].getSizeX()) * i) + sizeRectX, rectHeigth + sizeRectY);
        }
    }

    /**
     * Set the actual human state
     *
     * @param state State of the human , can be ON_ACTION,STOPPED or WALKING
     */
    public void setHumanState(HumanState state) {
        this.humanState = state;
    }

    /**
     * Set if the actual human is selected or no
     *
     * @return actual human is selected or no
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     * Get size Y of the human box
     *
     * @return size Y of the human box
     */
    @Override
    public int getSizeX() {
        return this.sizeX;
    }

    /**
     * Get size X of the human box
     *
     * @return size X of the human box
     */
    @Override
    public int getSizeY() {
        return this.sizeY;
    }

    /**
     * Set if a human is selected right now or no
     *
     * @param selected indicate if a human is selected right now or no
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if(!selected){
            setSelecttingMode(false);
        }
    }

    /**
     * Create actionBitmap,exitBitmap and bitmapSoldier,bitmapVillager or bitmapConstructor
     * depending on the humanType
     */
    public void setUnitsBitmaps() {
        switch (this.humanType) {
            case SOLDIER:
                this.bitmapSoldier = bitmapManager.getBitmapSoldier();
                this.actionBitmap = bitmapManager.getBitmapActionSword();
                break;

            case VILLAGER:
                this.bitmapVillager = bitmapManager.getBitmapVillager();
                this.actionBitmap = bitmapManager.getBitmapActionHandWhite();
                break;

            case CONSTRUCTOR:
                this.bitmapConstructor = bitmapManager.getBitmapConstructor();
                this.actionBitmap = bitmapManager.getBitmapActionHandWhite();
                break;

            case ENEMY:
                this.bitmapEnemy = bitmapManager.getBitmapEnemy();
                this.actionBitmap = bitmapManager.getBitmapActionSword();
                break;
        }
        this.exitBitmap = BitmapManager.getBitmapFromAssets("BarIcons/red_boxCross.png", context);
        this.exitBitmap = BitmapManager.scaleByHeight(this.exitBitmap, this.boxes[0].getSizeY() * sizeY);
    }

    /**
     * Set a DrawObjectType and Subtype on the actualBox depending on the humanType.
     * In addition create all the bitmaps needed to draw a human in all states.
     *
     * @param box       Box to modify.
     * @param humanType HumanType using to set DrawObjectType and Subtype in the box
     */
    private void createHuman(int box, HumanType humanType) {
        switch (humanType) {
            case VILLAGER:
                humanWalking = bitmapManager.getVillagerWalking();
                humanAction = bitmapManager.getVillagerAction();
                humanDead = new HashMap<HumanOrientation, Bitmap[]>();
                break;

            case CONSTRUCTOR:
                humanWalking = bitmapManager.getConstructorWalking();
                humanAction = bitmapManager.getConstructorAction();
                humanDead = new HashMap<HumanOrientation, Bitmap[]>();
                break;

            case SOLDIER:
                humanWalking = bitmapManager.getSoldierWalking();
                humanAction = bitmapManager.getSoldierAction();
                humanDead = bitmapManager.getSoldierDead();
                break;

            case ENEMY:
                humanWalking = bitmapManager.getEnemyWalking();
                humanAction = bitmapManager.getEnemyAction();
                humanDead = bitmapManager.getEnemyDead();
                break;
        }

    }

    /**
     * Move a human inside on the actualBox
     */
    public void moveHumanOnActualBox() {
        int indexX = boxes[actualBox].getActualGameObjectIndexX();
        int indexY = boxes[actualBox].getActualGameObjectIndexY();

        if (indexX > boxes[actualBox].getMiddleIndexX()) {
            humanOrientation = HumanOrientation.WEST;
            boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX() - 1);
        } else if (indexX < boxes[actualBox].getMiddleIndexX()) {
            humanOrientation = HumanOrientation.EST;
            boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX() + 1);
        } else if (indexY != 0) {
            humanOrientation = HumanOrientation.NORTH;
            boxes[actualBox].setActualGameObjectIndexY(boxes[actualBox].getActualGameObjectIndexY() - 1);
        } else {
            if(boxMiddleDestiny < 0) {
                setOrientationAction();
                boxDestiny = -1;
            }else{
                boxMiddleDestiny = -1;
                lastIndexesWalked = new ArrayList<>();
            }
        }
    }

    /**
     * Move the human in any direction. If human finds a  obstacle, the function will move the human on the opposite direction.
     * When human rise boxDestiny, human will be stop itself.
     *
     * @param humanMovementType
     */
    public void moveHuman(HumanMovementType humanMovementType,int newBoxIndex) {
        int actualBoxIndexX = 0, actualBoxIndexY = 0;
        switch (humanMovementType) {
            case VERTICAL_UP:
                if (boxes[actualBox].getActualGameObjectIndexY() == 0) {
                    boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                    actualBoxIndexX = boxes[actualBox].getActualGameObjectIndexX();
                    lastBox = actualBox;
                    actualBox = newBoxIndex;
                    boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, drawObjectSubtype, this);
                    boxes[actualBox].setActualGameObjectIndexY(boxes[actualBox].getMovingYSize() - 1);
                    boxes[actualBox].setActualGameObjectIndexX(actualBoxIndexX);
                    contMoveDirection = 0;
                } else {
                    contMoveDirection++;
                    boxes[actualBox].setActualGameObjectIndexY(boxes[actualBox].getActualGameObjectIndexY() - 1);
                }
                break;

            case VERTICAL_DOWN:
                if (boxes[actualBox].getActualGameObjectIndexY() == boxes[actualBox].getMovingYSize() - 1) {
                    boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                    actualBoxIndexX = boxes[actualBox].getActualGameObjectIndexX();
                    actualBox = newBoxIndex;
                    boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, drawObjectSubtype, this);
                    boxes[actualBox].setActualGameObjectIndexY(0);
                    boxes[actualBox].setActualGameObjectIndexX(actualBoxIndexX);
                    contMoveDirection = 0;
                } else {
                    contMoveDirection++;
                    boxes[actualBox].setActualGameObjectIndexY(boxes[actualBox].getActualGameObjectIndexY() + 1);
                }
                break;

            case HORIZONTAL_LEFT:
                if (boxes[actualBox].getActualGameObjectIndexX() == 0) {
                    boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                    actualBoxIndexY = boxes[actualBox].getActualGameObjectIndexY();
                    lastBox = actualBox;
                    actualBox = newBoxIndex;
                    boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, drawObjectSubtype, this);
                    boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getMovingXSize() - 1);
                    boxes[actualBox].setActualGameObjectIndexY(actualBoxIndexY);
                    contMoveDirection = 0;
                } else {
                    contMoveDirection++;
                    boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX() - 1);
                }
                break;

            case HORIZONTAL_RIGHT:
                if (boxes[actualBox].getActualGameObjectIndexX() == boxes[actualBox].getMovingXSize() - 1) {
                    boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                    actualBoxIndexY = boxes[actualBox].getActualGameObjectIndexY();
                    lastBox = actualBox;
                    actualBox = newBoxIndex;
                    boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, drawObjectSubtype, this);
                    boxes[actualBox].setActualGameObjectIndexX(0);
                    boxes[actualBox].setActualGameObjectIndexY(actualBoxIndexY);
                    contMoveDirection = 0;
                } else {
                    contMoveDirection++;
                    boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX() + 1);
                }
                break;
        }
    }


    /**
     * Calculate orientation and direction of the human and move itself
     * in the direction and orientation calculated
     */
    private void setMovementDirection() {
        int boxToMove;
        if(boxMiddleDestiny >= 0){
            boxToMove = boxMiddleDestiny;
        }else{
            boxToMove = boxDestiny;
        }

        boolean condition1 = actualBox == boxToMove && (boxes[actualBox].getActualGameObjectIndexX() != boxes[actualBox].getMiddleIndexX() || boxes[actualBox].getActualGameObjectIndexY() != 0);
        boolean condition2 = actualBox != boxToMove && boxToMove >= 0;

        if (condition1 || condition2) {
            if (actualBox == boxToMove) {
                moveHumanOnActualBox();
                return;
            }

            if (actualBox == boxMiddleDestiny) {
                boxMiddleDestiny = -1;
                lastIndexesWalked = new ArrayList<>();
                return;
            }
            movingDifferenceX = boxes[actualBox].getIndexX() - boxes[boxToMove].getIndexX();
            movingDifferenceY = boxes[actualBox].getIndexY() - boxes[boxToMove].getIndexY();
            int differenceAbs = Math.abs(movingDifferenceX) - Math.abs(movingDifferenceY);
            String difference = "";

            if (differenceAbs == 0) {
                difference = "0";
            } else {
                difference = differenceAbs > 0 ? "horizontal" : "vertical";
            }

            int newBoxIndex,indexX = 0,indexY = 0;
            if(boxMiddleDestiny > 0 && difference.equals("0") && !checkHumanOrientation()){
                for (int i = 0; i < HumanOrientation.values().length; i++) {
                    if(humanOrientation.equals(HumanOrientation.values()[i])){
                        if(i == HumanOrientation.values().length - 1) {
                            humanOrientation = HumanOrientation.values()[0];
                            break;
                        }else{
                            humanOrientation = HumanOrientation.values()[i+1];
                            break;
                        }
                    }
                }
            }

            if(difference.equals("horizontal") && contMoveDirection >= Box.getMovingXSize()){
                difference = "vertical";
            }else if(difference.equals("vertical") && contMoveDirection >= Box.getMovingYSize()){
                difference = "horizontal";
            }


            switch (difference) {
                case "0":
                    lastDifference = "0";
                    switch (humanOrientation) {
                        case EST:
                            indexX = boxes[actualBox].getIndexX() + 1;
                            indexY = boxes[actualBox].getIndexY();
                            newBoxIndex = GameTools.getBoxByIndex(boxes, indexX,indexY);

                            if (newBoxIndex >=0 && boxes[newBoxIndex].getGameObject() == null && checkBox(indexX,indexY)) {
                                humanOrientation = HumanOrientation.EST;
                                moveHuman(HumanMovementType.HORIZONTAL_RIGHT,newBoxIndex);
                            }else {
                                getAlternativeBox(boxes[actualBox].getIndexX() + 1,boxes[actualBox].getIndexY(),false,movingDifferenceY<=0);
                            }
                            break;

                        case WEST:
                            indexX = boxes[actualBox].getIndexX() - 1;
                            indexY = boxes[actualBox].getIndexY();
                            newBoxIndex = GameTools.getBoxByIndex(boxes, indexX,indexY);

                            if (newBoxIndex >=0 && boxes[newBoxIndex].getGameObject() == null && checkBox(indexX,indexY)) {
                                humanOrientation = HumanOrientation.WEST;
                                moveHuman(HumanMovementType.HORIZONTAL_LEFT,newBoxIndex);
                            }else {
                                getAlternativeBox(boxes[actualBox].getIndexX() - 1,boxes[actualBox].getIndexY(),false,movingDifferenceY<=0);
                            }
                            break;

                        case NORTH:
                            indexX = boxes[actualBox].getIndexX();
                            indexY = boxes[actualBox].getIndexY() - 1;
                            newBoxIndex = GameTools.getBoxByIndex(boxes, indexX,indexY);

                            if (newBoxIndex >=0 && boxes[newBoxIndex].getGameObject() == null && checkBox(indexX,indexY)) {
                                humanOrientation = HumanOrientation.NORTH;
                                moveHuman(HumanMovementType.VERTICAL_UP,newBoxIndex);
                            }else {
                                getAlternativeBox(boxes[actualBox].getIndexX() ,boxes[actualBox].getIndexY()-1,true,movingDifferenceX<=0);
                            }
                            break;

                        case SOUTH:
                            indexX = boxes[actualBox].getIndexX();
                            indexY = boxes[actualBox].getIndexY() + 1;
                            newBoxIndex = GameTools.getBoxByIndex(boxes, indexX,indexY);

                            if (newBoxIndex >=0 && boxes[newBoxIndex].getGameObject() == null && checkBox(indexX,indexY)) {
                                humanOrientation = HumanOrientation.SOUTH;
                                moveHuman(HumanMovementType.VERTICAL_DOWN,newBoxIndex);
                            }else {
                                getAlternativeBox(boxes[actualBox].getIndexX() ,boxes[actualBox].getIndexY()+1,true,movingDifferenceX<=0);
                            }
                            break;
                    }
                    break;

                case "horizontal":
                    lastDifference = "horizontal";
                    if (movingDifferenceX >= 0) {
                        indexX = boxes[actualBox].getIndexX() - 1;
                        indexY = boxes[actualBox].getIndexY();
                        newBoxIndex = GameTools.getBoxByIndex(boxes, boxes[actualBox].getIndexX() - 1, boxes[actualBox].getIndexY());
                        if (newBoxIndex >= 0 && boxes[newBoxIndex].getGameObject() == null && checkBox(indexX,indexY)) {
                            humanOrientation = HumanOrientation.WEST;
                            moveHuman(HumanMovementType.HORIZONTAL_LEFT,newBoxIndex);
                        }else {
                            getAlternativeBox(boxes[actualBox].getIndexX() - 1,boxes[actualBox].getIndexY(),false,movingDifferenceY<=0);
                        }
                    } else {
                        indexX = boxes[actualBox].getIndexX() + 1;
                        indexY = boxes[actualBox].getIndexY();
                        newBoxIndex = GameTools.getBoxByIndex(boxes, boxes[actualBox].getIndexX() + 1, boxes[actualBox].getIndexY());
                        if (newBoxIndex >= 0 && boxes[newBoxIndex].getGameObject() == null && checkBox(indexX,indexY)) {
                            humanOrientation = HumanOrientation.EST;
                            moveHuman(HumanMovementType.HORIZONTAL_RIGHT,newBoxIndex);
                        }else {
                            getAlternativeBox(boxes[actualBox].getIndexX() + 1,boxes[actualBox].getIndexY(),false,movingDifferenceY<=0);
                        }
                    }
                    break;

                case "vertical":
                    lastDifference = "vertical";
                    if (movingDifferenceY <= 0) {
                        indexX = boxes[actualBox].getIndexX();
                        indexY = boxes[actualBox].getIndexY() + 1;
                        newBoxIndex = GameTools.getBoxByIndex(boxes, boxes[actualBox].getIndexX(), boxes[actualBox].getIndexY()+ 1);
                        if (newBoxIndex >=0 && boxes[newBoxIndex].getGameObject() == null && checkBox(indexX,indexY)) {
                            humanOrientation = HumanOrientation.SOUTH;
                            moveHuman(HumanMovementType.VERTICAL_DOWN,newBoxIndex);
                        }else {
                            getAlternativeBox(boxes[actualBox].getIndexX() ,boxes[actualBox].getIndexY()-1,true,movingDifferenceX<=0);
                        }
                    } else {
                        indexX = boxes[actualBox].getIndexX();
                        indexY = boxes[actualBox].getIndexY() - 1;
                        newBoxIndex = GameTools.getBoxByIndex(boxes, boxes[actualBox].getIndexX(), boxes[actualBox].getIndexY()- 1);
                        if (newBoxIndex >=0 && boxes[newBoxIndex].getGameObject() == null && checkBox(indexX,indexY)) {
                            humanOrientation = HumanOrientation.NORTH;
                            moveHuman(HumanMovementType.VERTICAL_UP,newBoxIndex);
                        }else {
                            getAlternativeBox(boxes[actualBox].getIndexX() ,boxes[actualBox].getIndexY()+1,true,movingDifferenceX<=0);
                        }
                    }
                    break;
            }

        } else {
            if (actualBox == boxMiddleDestiny) {
                boxMiddleDestiny = -1;
                lastIndexesWalked = new ArrayList<>();
                return;
            }else {
                if (this.humanState == HumanState.ONACTION) {
                    boxDestiny = -1;
                } else if (this.humanState == HumanState.WALKING) {
                    this.humanState = HumanState.STTOPED;
                    boxDestiny = -1;
                }
            }
        }
    }

    /**
     * Calculates boxDestiny variable when a OnTouchEvent is produced
     * when a human is selected
     *
     * @param boxIndex
     * @return
     */
    @Override
    public int onTouchWhenSelected(int boxIndex) {
        this.boxDestiny = boxIndex;
        this.objectObjetive = boxes[boxIndex].getGameObject();

        if (boxes[boxIndex].getGameObject() == null && selecttingMode) {
            this.humanState = HumanState.WALKING;
        } else if (selecttingMode) {
            this.humanState = HumanState.ONACTION;
            this.boxDestiny = boxIndex - 1;
        }
        return actualBox;
    }

    @Override
    public boolean isSelectingMode() {
        return selecttingMode;
    }

    /**
     * Set the human on selecting mode or no
     *
     * @param selectingMode true selectingMode on, false selecting mode off
     */
    @Override
    public void setSelectingMode(boolean selectingMode) {
        this.selecttingMode = selectingMode;
    }

    /**
     * Manage the scrren touch on a human
     *
     * @param selecttingMode actual selectingMode enabled or no
     * @param x              screen x
     * @param y              screen y
     * @param boxSelected    actual box selected or no
     */
    @Override
    public void onTouchObject(boolean selecttingMode, int x, int y, int boxSelected) {
        if (!this.selected) {
            if (!selecttingMode) {
                GameTools.deselectedAll(boxes);
                this.setSelected(true);
            }
        } else {
            if (y >= drawActionsBar.getInitY()) {
                onTouchActionBarObject(x, y);
            } else if (selecttingMode) {
                objectObjetive = boxes[boxSelected].getGameObject();
                boxDestiny = boxSelected - 1;
                humanState = HumanState.ONACTION;
                setSelectingMode(false);
            } else {
                boxDestiny = boxSelected;
                humanState = HumanState.WALKING;
                if(boxes[boxDestiny].getGameObject() != null){
                    int index = boxDestiny;
                    while (boxes[index].getGameObject() != null){
                        index++;
                        if(index >= boxes.length){
                            index = 0;
                        }
                    }
                }
            }
        }
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

    /**
     * Get the actual life of the human.
     *
     * @return actual life of the human.
     */
    public int getActualLife() {
        return actualLife;
    }

    /**
     * Set the actual life of the human.
     *
     * @param actualLife
     */
    public void setActualLife(int actualLife) {
        this.actualLife = actualLife;
        if (actualLife <= 0) {
            humanState = HumanState.DEAD;
        }
    }

    /**
     * Do the human action in depends of the HumanType. The human action is received by a GameObject
     *
     * @param gameObject GameObject that receives the human action.
     */
    public void doAction(GameObject gameObject) {
        switch (humanType) {
            case CONSTRUCTOR:
                if (gameObject != null && gameObject.getClass().equals(Building.class)) {
                    if(drawResourcesBar.getActualWood() > WOOD_TO_REPAIR && drawResourcesBar.getActualStone() > STONE_TO_CONSTRUCT) {
                        if (((Building) gameObject).getActualLife() < ((Building) gameObject).getInitLife()) {
                            switch (((Building) gameObject).getBuildingType()) {
                                case MAIN:
                                    drawResourcesBar.setActualWood(drawResourcesBar.getActualWood()-WOOD_TO_REPAIR);
                                    drawResourcesBar.setActualStone(drawResourcesBar.getActualStone()-STONE_TO_CONSTRUCT);
                                    ((Building) gameObject).setActualLife(((Building) gameObject).getActualLife() + REPAIR);

                                    break;
//                                case TOWER:
//                                    if (drawResourcesBar.getActualWood() >= REPAIR) {
//                                        ((Building) gameObject).setActualLife(((Building) gameObject).getActualLife() + REPAIR);
//                                    }
//                                    break;
                            }
                        } else {
                            flagActionEnd = true;
                        }
                    }else {
                        flagActionEnd = true;
                    }
                } else {
//                    if (drawResourcesBar.getActualWood() > STONE_TO_CONSTRUCT) {
//                        int indexX = boxes[actualBox].getIndexX();
//                        int indexY = boxes[actualBox].getIndexY();
//                        if (boxes[GameTools.getBoxByIndex(boxes, indexX - 1, indexY)].getGameObject() == null) {
//                            boxes[GameTools.getBoxByIndex(boxes, indexX - 1, indexY)].
//                                    setDrawObjectTypeAndSubtype(DrawObjectType.NATURE, DrawObjectSubtype.TOWER,
//                                            new Building(boxes, 0, GameTools.getBoxByIndex(boxes, indexX - 1, indexY), context, BuildingType.TOWER,
//                                                    drawActionsBar, drawResourcesBar, bitmapManager));
//                        } else if (boxes[GameTools.getBoxByIndex(boxes, indexX + 1, indexY)].getGameObject() == null) {
//                            boxes[GameTools.getBoxByIndex(boxes, indexX + 1, indexY)].
//                                    setDrawObjectTypeAndSubtype(DrawObjectType.NATURE, DrawObjectSubtype.TOWER,
//                                            new Building(boxes, 0, GameTools.getBoxByIndex(boxes, indexX + 1, indexY), context, BuildingType.TOWER,
//                                                    drawActionsBar, drawResourcesBar, bitmapManager));
//                        } else if (boxes[GameTools.getBoxByIndex(boxes, indexX, indexY - 1)].getGameObject() == null) {
//                            boxes[GameTools.getBoxByIndex(boxes, indexX, indexY - 1)].
//                                    setDrawObjectTypeAndSubtype(DrawObjectType.NATURE, DrawObjectSubtype.TOWER,
//                                            new Building(boxes, 0, GameTools.getBoxByIndex(boxes, indexX, indexY - 1), context, BuildingType.TOWER,
//                                                    drawActionsBar, drawResourcesBar, bitmapManager));
//                        } else if (boxes[GameTools.getBoxByIndex(boxes, indexX, indexY + 1)].getGameObject() == null) {
//                            boxes[GameTools.getBoxByIndex(boxes, indexX - 1, indexY + 1)].
//                                    setDrawObjectTypeAndSubtype(DrawObjectType.NATURE, DrawObjectSubtype.TOWER,
//                                            new Building(boxes, 0, GameTools.getBoxByIndex(boxes, indexX, indexY + 1), context, BuildingType.TOWER,
//                                                    drawActionsBar, drawResourcesBar, bitmapManager));
//                        }
//                        flagActionEnd = true;
//                    }else {
//                        flagActionEnd = true;
//                    }
                }
                break;

            case VILLAGER:
                if (gameObject.getClass().equals(Nature.class)) {
                    if (((Nature) gameObject).getActualResources() > 0) {
                        ((Nature) gameObject).setActualResources(((Nature) gameObject).getActualResources() - COLLECT);
                        switch (((Nature) gameObject).getNatureType()) {
                            case FOOD:
                                drawResourcesBar.setActualFood(drawResourcesBar.getActualFood() + COLLECT);
                                break;

                            case ROCK:
                                drawResourcesBar.setActualStone(drawResourcesBar.getActualStone() + COLLECT);
                                break;

                            case WOOD:
                                drawResourcesBar.setActualWood(drawResourcesBar.getActualWood() + COLLECT);
                                break;
                        }
                    } else {
                        flagActionEnd = true;
                    }
                }
                break;

            case SOLDIER:
                if(gameObject != null) {
                    if (gameObject.getClass().equals(Human.class) && ((Human) gameObject).getHumanType() == HumanType.ENEMY) {
                        ((Human) gameObject).setActualLife(((Human) gameObject).getActualLife() - ATACK);
                        if (((Human) gameObject).getActualLife() <= 0) {
                            drawActionsBar.setEnemiesDead(drawActionsBar.getEnemiesDead()+1);
                            flagActionEnd = true;
                        }
                    }
                }
                break;

            case ENEMY:
                if(gameObject != null) {
                    if (gameObject.getClass().equals(Building.class) && ((Building) gameObject).getBuildingType() == BuildingType.MAIN) {
                        ((Building) gameObject).setActualLife(((Building) gameObject).getActualLife() - ATACK);
                        if (((Building) gameObject).getActualLife() <= 0) {
                            flagActionEnd = true;
                        }
                    }
                }
                break;
        }
    }

    /**
     * Get the type of human
     *
     * @return type of human
     */
    public HumanType getHumanType() {
        return humanType;
    }

    public boolean isSelecttingMode() {
        return selecttingMode;
    }

    /**
     * Set the human on selecting mode or no
     *
     * @param selecttingMode true selectingMode on, false selecting mode off
     */
    public void setSelecttingMode(boolean selecttingMode) {
        this.selecttingMode = selecttingMode;
        if (selecttingMode) {
            if(humanType == HumanType.SOLDIER ) {
                actionBitmap = bitmapManager.getBitmapActionSwordBrown();
            }else{
                actionBitmap = bitmapManager.getBitmapActionHandBrown();
            }
        } else {
            if(humanType == HumanType.SOLDIER ) {
                actionBitmap = bitmapManager.getBitmapActionSword();
            }else{
                actionBitmap = bitmapManager.getBitmapActionHandWhite();
            }
        }

    }

    /**
     * Set a box destiny to the human actual.
     *
     * @param boxDestiny box index that will be the human destiny
     */
    public void setBoxDestiny(int boxDestiny) {
        if (boxes[boxDestiny].getGameObject() == null) {
            this.boxDestiny = boxDestiny;
        } else {
            this.boxDestiny = boxDestiny - 1;
            objectObjetive = boxes[boxDestiny].getGameObject();
        }
    }

    /**
     * Get an alternative box to move the human. The box choose will be the next box
     * in the same direction of the human witch no contains an object,until a max of 3.
     * @param indexX box indexX that the human needs to dodge
     * @param indexY box indexY that the human needs to dodge
     * @param xOrY true - search a box on index X, true - search a box on index Y
     * @param incrementOrDecrement true - increment index, false - decrement index
     */
    private void getAlternativeBox(int indexX,int indexY,boolean xOrY,boolean incrementOrDecrement){
        int index;
        boolean flagBoxWalked = false;

        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if (xOrY) {
                    if (incrementOrDecrement) {
                        index = GameTools.getBoxByIndex(boxes, indexX + i, indexY +j);
                    } else {
                        index = GameTools.getBoxByIndex(boxes, indexX - i, indexY -j);
                    }
                } else {
                    if (incrementOrDecrement) {
                        index = GameTools.getBoxByIndex(boxes, indexX +j, indexY + i);
                    } else {
                        index = GameTools.getBoxByIndex(boxes, indexX -j, indexY - i);
                    }
                }


                if(lastIndexesWalked.size() > 0){
                    for (int k = 0; k < lastIndexesWalked.size(); k++) {
                        if(lastIndexesWalked.get(k) == index){
                            flagBoxWalked = true;
                        }
                    }
                }

                if (checkBox(boxes[index].getIndexX(),boxes[index].getIndexY()) && boxes[index].getGameObject() == null && !flagBoxWalked) {
                    boxMiddleDestiny = index;
                    lastIndexesWalked.add(index);
                    return;
                }
            }
        }
    }

    /**
     * Set the human orientation. Human must be oriented to object objective.
     */
    private void setOrientationAction(){
        if(objectObjetive == null){
            return;
        }
        int indexObjectiveX = boxes[objectObjetive.getActualBox()].getIndexX();
        int indexObjectiveY = boxes[objectObjetive.getActualBox()].getIndexY();
        int indexActualX = boxes[actualBox].getIndexX();
        int indexActualY = boxes[actualBox].getIndexY();

        if(indexObjectiveY == indexActualY){
            if(indexActualX < indexObjectiveX){
                humanOrientation = HumanOrientation.EST;
            }
            else{
                humanOrientation = HumanOrientation.WEST;
            }
        }else  if(indexObjectiveX == indexActualX){
            if(indexActualY < indexObjectiveY){
                humanOrientation = HumanOrientation.SOUTH;
            }
            else{
                humanOrientation = HumanOrientation.NORTH;
            }
        }
    }

    /**
     * Set object objective to do the action f the human
     * @return object objective to do the action f the human
     */
    public void setObjectObjetive(GameObject objectObjetive) {
        this.objectObjetive = objectObjetive;
    }

    /**
     * Set a box destiny for a constructor or an enemy
     */
    private void setEnemyOrConstructorDestiny() {
        if(boxes[boxDestiny].getGameObject() == null){
            return;
        }
        int[] boxesLimit = Escenario.getBoxesMainBuild();
        for (int i = 0; i < boxesLimit.length; i++) {
            if (boxes[boxesLimit[i]].getGameObject() == null) {
                this.setBoxDestiny(boxesLimit[i]);
//                this.boxMiddleDestiny = -1;
                return;
            }
        }
    }

    /**
     * Chaeck if a box exists into array boxes
     * @param indexX index X of the box
     * @param indexY index Y of the box
     * @return true if the box exist, false, the box not exist
     */
    private boolean checkBox(int indexX, int indexY){
        if(indexX < 0 || indexY < 0){
            return false;
        }else if(indexX >= Box.getDIV() || indexY >= Box.getDIV()){
            return false;
        }

        return true;
    }

    /**
     * Check the human orientation depending of the next box in the direction of the orientation, loking if
     * this box has a GameObject
     * @return true - the box has a GameObject, false - the box doesn't have a GameObject
     */
    private boolean checkHumanOrientation(){
        if(humanOrientation == HumanOrientation.EST){
            return boxes[GameTools.getBoxByIndex(boxes,boxes[actualBox].getIndexX() + 1,boxes[actualBox].getIndexY())].getGameObject() == null;
        }else if(humanOrientation == HumanOrientation.WEST){
            return boxes[GameTools.getBoxByIndex(boxes,boxes[actualBox].getIndexX() - 1,boxes[actualBox].getIndexY())].getGameObject() == null;
        }else if(humanOrientation == HumanOrientation.NORTH){
            return boxes[GameTools.getBoxByIndex(boxes,boxes[actualBox].getIndexX(),boxes[actualBox].getIndexY() - 1)].getGameObject() == null;
        }else if(humanOrientation == HumanOrientation.SOUTH){
            return boxes[GameTools.getBoxByIndex(boxes,boxes[actualBox].getIndexX(),boxes[actualBox].getIndexY() + 1)].getGameObject() == null;
        }

        return false;
    }

    private void changeSoldierOrCollectorDestiny(){
        if(boxes[boxDestiny].getGameObject() == null){
            if(boxes[boxDestiny].getGameObject() == objectObjetive){
                setHumanState(HumanState.ONACTION);
                boxDestiny = -1;
            }else {
                return;
            }
        }
        int[] boxesLimit = new int[4];
        boxesLimit[0] = GameTools.getBoxByIndex(boxes,boxes[boxDestiny].getIndexX()-1,boxes[boxDestiny].getIndexY());
        boxesLimit[1] = GameTools.getBoxByIndex(boxes,boxes[boxDestiny].getIndexX()+1,boxes[boxDestiny].getIndexY());
        boxesLimit[2] = GameTools.getBoxByIndex(boxes,boxes[boxDestiny].getIndexX(),boxes[boxDestiny].getIndexY()-1);
        boxesLimit[3] = GameTools.getBoxByIndex(boxes,boxes[boxDestiny].getIndexX(),boxes[boxDestiny].getIndexY()+1);

        for (int i = 0; i < boxesLimit.length; i++) {
            if( actualBox ==  boxesLimit[i]){
                boxDestiny = -1;
                return;
            }
            if (boxesLimit[i]!= -1 && boxes[boxesLimit[i]].getGameObject() == this || boxesLimit[i]!= -1 && boxes[boxesLimit[i]].getGameObject() == objectObjetive ) {
                this.setBoxDestiny(boxesLimit[i]);
                return;
            }
        }

        for (int i = 0; i < boxesLimit.length; i++) {
            if (boxesLimit[i]!= -1 && boxes[boxesLimit[i]].getGameObject() == null ) {
                this.setBoxDestiny(boxesLimit[i]);
                return;
            }
        }
    }
}