package com.example.battlekings.GameManger;

import android.content.Context;
import com.example.battlekings.DrawObjects.GameObject;
import com.example.battlekings.DrawObjects.buildings.Building;
import com.example.battlekings.DrawObjects.buildings.BuildingType;
import com.example.battlekings.DrawObjects.gameBars.DrawActionsBar;
import com.example.battlekings.DrawObjects.gameBars.DrawResourcesBar;
import com.example.battlekings.DrawObjects.nature.Nature;
import com.example.battlekings.DrawObjects.nature.NatureType;
import com.example.battlekings.Screen.Box;
import com.example.battlekings.Screen.PointIndex;
import com.example.battlekings.Utils.BitmapManager;
import com.example.battlekings.Utils.GameTools;
import java.util.ArrayList;

/**
 * Used to put a grouping Gameobjects on a boxes array in a specified form
 */
public class Escenario {
    /** Game Objects total , to put on a boxes array   */
    private static final int OBJECTS_TOTAL = 10;
    /** Number max of boxes horizonal or vertial*/
    private static final int DIV = 20;
    /** Nuber of boxes between the player init indexes and the indexes to start to draw objects*/
    private static final int SPACE = 6;
    /** Index x where the payer init the game*/
    private static final int PLAYER_INIT_X = 20;
    /** Index y where the payer init the game*/
    private static final int PLAYER_INIT_Y = 20;
    /** Index x where the main build init the game*/
    private static final int MAIN_BUILDING_INIT_X = 16;
    /** Index y where the main build  init the game*/
    private static final int MAIN_BUILDING_INIT_Y = 16;

    /** indexes occupied by the objects*/
    private ArrayList<PointIndex> indexesOccupied;
    /** objects to draw on the screen*/
    private ArrayList<GameObject> objectsToDraw;
    /** application context*/
    private Context context;
    /** Arraay of boxes where put the objects */
    private static Box[] boxes;
    /** Game DrawActionsBar*/
    private DrawActionsBar drawActionsBar;
    /** Game DrawResourcesBar*/
    private DrawResourcesBar drawResourcesBar;
    /** Index of 'boxes' where the main building is allocated*/
    private int mainBuildingBox;
    /** Class to get all bitmaps of the game*/
    private BitmapManager bitmapManager;
    /** boxes witch the main build occupied*/
    private static int[] boxesMainBuild;

    public Escenario(Context context, Box[] boxes, DrawActionsBar drawActionsBar, DrawResourcesBar drawResourcesBar, BitmapManager bitmapManager) {
        this.bitmapManager = bitmapManager;
        this.context = context;
        this.boxes = boxes;
        this.drawActionsBar = drawActionsBar;
        this.objectsToDraw = new ArrayList<>();
        this.indexesOccupied = new ArrayList<>();
        this.drawResourcesBar = drawResourcesBar;
        initMainBuildingBoxes();
    }

    /**
     * Generate a random map. Put rocks, food and tress in aleatory boxes,
     * until a max of objects specified on the constant 'OBJECTS_TOTAL'
     */
    public void generateRandomScenario(){
        boolean flagContinue = true;

        while (indexesOccupied.size() < OBJECTS_TOTAL){
            int x = (int)(Math.random()*(DIV ));
            int y = (int)(Math.random()*(DIV ));
            int type = (int)(Math.random()*3);

            if(x > (DIV-SPACE) && y > (DIV-SPACE)){
                flagContinue = false;
            }

            if(flagContinue) {
                for (int i = 0; i < indexesOccupied.size(); i++) {
                    if (indexesOccupied.get(i).getIndexX() == x && indexesOccupied.get(i).getIndexY() == y) {
                        flagContinue = false;
                    }
                    //Controla que rocas y árboles no se solapen
                    if (indexesOccupied.get(i).getIndexY() == y - 1 || indexesOccupied.get(i).getIndexY() ==y +1) {
                        flagContinue = false;
                    } else if (indexesOccupied.get(i).getIndexX() == x - 1 || indexesOccupied.get(i).getIndexX() == x+1) {
                        flagContinue = false;
                    }
                }

                if (flagContinue) {
                    if (x < PLAYER_INIT_X || y < PLAYER_INIT_Y) {
                        if (type == 0) {
                            objectsToDraw.add(new Nature(boxes, 0, GameTools.getBoxByIndex(boxes, x, y), context, NatureType.WOOD, drawActionsBar, bitmapManager));
                            indexesOccupied.add(new PointIndex(x, y));
                        } else if (type == 1) {
                            objectsToDraw.add(new Nature(boxes, 0, GameTools.getBoxByIndex(boxes, x, y), context, NatureType.ROCK, drawActionsBar, bitmapManager));
                            indexesOccupied.add(new PointIndex(x, y));
                        } else {
                            objectsToDraw.add(new Nature(boxes, 0, GameTools.getBoxByIndex(boxes, x, y), context, NatureType.FOOD, drawActionsBar, bitmapManager));
                            indexesOccupied.add(new PointIndex(x, y));
                        }
                    }
                } else {
                    flagContinue = true;
                }
            }else {
                flagContinue = true;
            }
        }

        generateMainBuilding(MAIN_BUILDING_INIT_X,MAIN_BUILDING_INIT_Y);
    }

    /**
     * Generate the main building and in the boxes array
     * @param initIndexX indexX of the init box of the main building
     * @param initIndexY indexY of the init box of the main building
     */
    public void generateMainBuilding(int initIndexX,int initIndexY){
        mainBuildingBox = GameTools.getBoxByIndex(boxes,initIndexX,initIndexY);
        Building mainBuilding = new Building(boxes,0,GameTools.getBoxByIndex(boxes,initIndexX,initIndexY),context, BuildingType.MAIN, drawActionsBar,drawResourcesBar,bitmapManager);
        objectsToDraw.add(new Building(boxes,0,GameTools.getBoxByIndex(boxes,initIndexX,initIndexY),context, BuildingType.MAIN, drawActionsBar,drawResourcesBar,bitmapManager));
        boxes[GameTools.getBoxByIndex(boxes,initIndexX+1,initIndexY)].setGameObject(mainBuilding);
        boxes[GameTools.getBoxByIndex(boxes,initIndexX,initIndexY+1)].setGameObject(mainBuilding);
        boxes[GameTools.getBoxByIndex(boxes,initIndexX+1,initIndexY+1)].setGameObject(mainBuilding);
//        indexID++;
//        objectsToDraw.add(new Building(boxes,indexID,GameTools.getBoxByIndex(boxes,TOWER_INIT_X,TOWER_INIT_Y),context, BuildingType.TOWER, drawActionsBar,drawResourcesBar,bitmapManager));
    }

    /**
     * Get the boxes array of the game
     * @return
     */
    public Box[] getBoxes() {
        return boxes;
    }

    /**
     * Get the init box where the main building, starts to draw
     * @return init box of the main building.
     */
    public int getMainBuildingBox() {
        return mainBuildingBox;
    }

    public static int[] getBoxesMainBuild() {
        return boxesMainBuild;
    }

    /**
     * Charge the index boxes witch that limit with the main build
     * @return Charge the index boxes witch that limit with the main build
     */
    public void initMainBuildingBoxes() {
        boxesMainBuild = new int[8];
        boxesMainBuild[0] = GameTools.getBoxByIndex(boxes,MAIN_BUILDING_INIT_X-1,MAIN_BUILDING_INIT_Y);
        boxesMainBuild[1] = GameTools.getBoxByIndex(boxes,MAIN_BUILDING_INIT_X-1,MAIN_BUILDING_INIT_Y-1);
        boxesMainBuild[2] = GameTools.getBoxByIndex(boxes,MAIN_BUILDING_INIT_X,MAIN_BUILDING_INIT_Y-1);
        boxesMainBuild[3] = GameTools.getBoxByIndex(boxes,MAIN_BUILDING_INIT_X+1,MAIN_BUILDING_INIT_Y-1);
        boxesMainBuild[4] = GameTools.getBoxByIndex(boxes,MAIN_BUILDING_INIT_X,MAIN_BUILDING_INIT_Y+2);
        boxesMainBuild[5] = GameTools.getBoxByIndex(boxes,MAIN_BUILDING_INIT_X+1,MAIN_BUILDING_INIT_Y+2);
        boxesMainBuild[6] = GameTools.getBoxByIndex(boxes,MAIN_BUILDING_INIT_X+2,MAIN_BUILDING_INIT_Y);
        boxesMainBuild[7] = GameTools.getBoxByIndex(boxes,MAIN_BUILDING_INIT_X+2,MAIN_BUILDING_INIT_Y+1);
    }
}
