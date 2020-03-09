package com.example.battlekings.Screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.battlekings.DrawObjects.DrawObjectSubtype;
import com.example.battlekings.DrawObjects.DrawObjectType;
import com.example.battlekings.DrawObjects.buildings.Building;
import com.example.battlekings.DrawObjects.buildings.BuildingType;
import com.example.battlekings.DrawObjects.gameBars.DrawActionsBar;
import com.example.battlekings.DrawObjects.gameBars.DrawResourcesBar;
import com.example.battlekings.DrawObjects.humans.Human;
import com.example.battlekings.DrawObjects.humans.HumanOrientation;
import com.example.battlekings.DrawObjects.humans.HumanState;
import com.example.battlekings.DrawObjects.humans.HumanType;
import com.example.battlekings.GameManger.Escenario;
import com.example.battlekings.MainActivity;
import com.example.battlekings.Utils.BitmapManager;
import com.example.battlekings.Utils.GameTools;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int DIVIDER_MIN_SECONDS = 5000;
    private static final int DIVIDER_SECONDS_INIT = 100000;

//    private ScaleGestureDetector mScaleGestureDetector;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private GameThread gameThread;
    private boolean runnig = true;
    Paint p ;
    public static final int DIV = 20;
    public static final int SIZEGAME = 2;
    public static final int ONSCREENINIT = 10;
    private DrawActionsBar drawActionsBar;
    private DrawResourcesBar drawResourcesBar;
    private int indexSelectedX = 0;
    private int indexSelectedY = 0;
    private boolean flagInit = true,flagMove = false;
    private int w;
    private int h;
    private int historicalX = -1,historicalY = -1,actualX =-1,actualY = -1,totalX = 0,totalY = 0;
    private ScreenDivider screenDivider;
    private BoxScreenManager boxScreenManager;
    private Box[] boxes,boxesToDraw;
    private int boxInit,boxGameObjectSelected;
    private Escenario escenario;
    private BitmapManager bitmapManager;
    private long timeMilInit;
    private int enemiesTotal = 0,enemiesSecondsDivider = DIVIDER_SECONDS_INIT;
    private MainActivity mainActivity;

    public GameView(Context context, MainActivity mainActivity) {
        super(context);
//        this.mScaleGestureDetector = mScaleGestureDetector;
        this.mainActivity = mainActivity;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.context = context;
        gameThread = new GameThread();
        setFocusable(true);
        timeMilInit = System.currentTimeMillis();
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.YELLOW);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }


    /**
     * Actions on surface changed. Init the thread if it is not inited.
     * Also charge the init configuration.
     * @param holder Surface holder of the surface view
     * @param format
     * @param width actual width of the screen
     * @param height actual height of the screen
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.w = width;
        this.h = height;
        getInitConfiguration();
        gameThread.setRunning(true);
        if (gameThread.getState() == Thread.State.NEW) gameThread.start();
        if (gameThread.getState() == Thread.State.TERMINATED) {
            gameThread = new GameThread();
            gameThread.start();
        }
    }

    /**
     * Executes when the surface is destroyed. Join the thead with the main thread.
     * @param holder Surface holder of the surface view
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainActivity.setScrenOrientation(false);
        mainActivity.setContentViewMain();
    }

    /**
     * Manage the Motions ACTION_DOWN,ACTION_MOVE,ACTION_UP.
     * On ACTION_MOVE moves the screen
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        mScaleGestureDetector.onTouchEvent(event);
//        synchronized (surfaceHolder) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    historicalX = (int) event.getX();
                    historicalY = (int) event.getY();
                    actualX = 0;
                    actualY = 0;

                case MotionEvent.ACTION_MOVE:


                    int moveX = 0, moveY = 0;
                    boolean up = false, left = false;
                    actualX = (int) event.getX();
                    actualY = (int) event.getY();
                    totalX += actualX - historicalX;
                    totalY += actualY - historicalY;


                    //Calculo de las casillas a mover
                    if (Math.abs(totalX) > boxes[0].getSizeX()) {
                        moveX = 1;
                        left = (actualX - historicalX) > 0;
                        totalX = 0;
                    }

                    if (Math.abs(totalY) > boxes[0].getSizeY()) {
                        moveY = 1;
                        up = (actualY - historicalY) > 0;
                        totalY = 0;
                    }

                    if (moveX > 0 || moveY > 0) {
                        moveScreen(moveX, moveY, up, left);
                    }

                    historicalX = (int) event.getX();
                    historicalY = (int) event.getY();


                    break;
                case MotionEvent.ACTION_UP:
                    historicalX = 0;
                    historicalY = 0;
                    int boxSelected = GameTools.getSelected(boxes);

                    if (event.getY() >= drawActionsBar.getInitY() && boxSelected >= 0) {
                        boxes[boxSelected].getGameObject().onTouchObject(false, (int) event.getX(), (int) event.getY(), boxSelected);
                    } else {
                        int boxTouchedIndex = getBoxBylocationForTouch((int) event.getX(), (int) event.getY());

                        if (boxTouchedIndex >= 0) {
                            Box box = boxes[boxTouchedIndex];
                            if (boxSelected > 0) {
                                if (boxes[boxSelected].getGameObject().isSelectingMode()) {
                                    boxes[boxSelected].getGameObject().onTouchObject(true, (int) event.getX(), (int) event.getY(), boxTouchedIndex);
                                } else {
                                    if (box.getGameObject() == null) {
                                        boxes[boxSelected].getGameObject().onTouchObject(false, (int) event.getX(), (int) event.getY(), boxTouchedIndex);
                                    } else {
                                        box.getGameObject().onTouchObject(false, (int) event.getX(), (int) event.getY(), boxSelected);
                                    }
                                }
                            } else if (box.getGameObject() != null) {
                                box.getGameObject().onTouchObject(false, (int) event.getX(), (int) event.getY(), boxSelected);
                            }
                        }
                    }
                    break;
            }
            return true;
//        }
    }

    /**
     * Draw the game.
     * At first the boxes and later the bars.
     * @param canvas
     */
    public void drawGame(Canvas canvas){
        drawVisibleBoxes(canvas);
        drawResourcesBar.draw(canvas);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw all the boxes of boxesToDraw
     * @param c Canvas
     */
    private void drawVisibleBoxes(Canvas c){
        if(((Building)escenario.getMainBuildingBox().getGameObject()).getActualLife() <= 0){
            runnig = false;
            return;
        }
        int indexBar = -1;
        //La superficie debe dibujarse primero
        for (int i = 0; i < boxScreenManager.getBoxesToDraw().length; i++) {
            boxesToDraw[i].drawFloor(c);
        }

        for (int i = 0; i < boxScreenManager.getBoxesToDraw().length; i++) {
            if(!(boxes[i].getGameObject() != null && boxes[i].getGameObject().getClass() == Human.class &&  ((Human)boxes[i].getGameObject()).getHumanType() == HumanType.ENEMY)) {
                boxesToDraw[i].drawBox(c);

                if (boxesToDraw[i].getGameObject() != null) {
                    if (boxesToDraw[i].getGameObject().isSelected()) {
                        c.drawRect(boxesToDraw[i].getX(), boxesToDraw[i].getY(), boxesToDraw[i].getFinalX(), boxesToDraw[i].getFinalY(), p);
                        indexBar = i;
                    }
                }

//            boxesOnGameChecker.draw(c,boxInit,ONSCREENINIT);
//            pruebas
//            Paint p = new Paint();
//            p.setStrokeWidth(5);
//
//            p.setColor(Color.RED);
//            p.setStyle(Paint.Style.STROKE);
//            c.drawRect(boxesToDraw[i].getX(), boxesToDraw[i].getY(), boxesToDraw[i].getFinalX(), boxesToDraw[i].getFinalY(),p);
//
//            p.setColor(Color.YELLOW);
//            p.setTextSize(boxesToDraw[i].getSizeY()/2);
//            c.drawText(boxesToDraw[i].xReference+":"+boxesToDraw[i].yReference,
//                    boxesToDraw[i].getX(),boxesToDraw[i].getY()+boxesToDraw[i].getSizeY(),p);
            }
        }

        if(indexBar > 0 &&  boxesToDraw[indexBar].getGameObject().isSelected()) {
            drawActionsBar.draw(c);
            boxesToDraw[indexBar].getGameObject().drawInActionBar(c);
        }
        boxesToDraw = boxScreenManager.updateBoxesTodraw(boxInit);

        if(enemiesTotal == 0){
            createEnemy();
            enemiesTotal++;
        }else if((System.currentTimeMillis() - timeMilInit) / enemiesSecondsDivider >= 1 && enemiesSecondsDivider > DIVIDER_MIN_SECONDS){
            timeMilInit = System.currentTimeMillis();
            createEnemy();
            enemiesTotal++;
            enemiesSecondsDivider = enemiesSecondsDivider/2;
        }
        movesEnemys(c);

    }

    /**
     * Create an  enemy on the box 0.
     */
    private void createEnemy(){
        boxes[0].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, DrawObjectSubtype.ENEMY,
                new Human(boxes,0,0,context, HumanType.ENEMY,drawActionsBar, HumanOrientation.SOUTH,drawResourcesBar,bitmapManager));
        ((Human)boxes[0].getGameObject()).setHumanState(HumanState.ONACTION);
        ((Human)boxes[0].getGameObject()).setBoxDestiny(getMainBuildingIndex());
    }

    /**
     * Move the enemy in main building direction
     * @param c Canvas
     */
    private void movesEnemys(Canvas c){
        for (int i = 0; i < boxes.length; i++) {
            if(boxes[i].getGameObject() != null && boxes[i].getGameObject().getClass() == Human.class &&  ((Human)boxes[i].getGameObject()).getHumanType() == HumanType.ENEMY){
                boxes[i].drawBox(c);
            }
        }
    }

    /**
     * Get the box witch contains a GameObject with the class Buildin.class
     * @return box witch contains a GameObject with the class Buildin.class
     */
    private int getMainBuildingIndex(){
        for (int i = 0; i < boxes.length; i++) {
            if(boxes[i].getGameObject() != null && boxes[i].getGameObject().getClass().equals(Building.class) && ((Building)boxes[i].getGameObject()).getBuildingType() == BuildingType.MAIN){
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the init box, where the game star to draw
     * @return box witch contains the hardware poins (0,0)
     */
    private int getInitBox(){
        for (int i = 0; i < boxes.length; i++) {
            if(boxes[i].getX() >= 0 && boxes[i].getY()>=0){
                return i-DIV-1;
            }
        }
        return -1;
    }

    /**
     * Get the init configuration necesary to begin the game.
     * Init the boxes array, and boxToDrawArray
     * Charge the two bars of the game 'drawResourcesBar' and 'drawActionsBar'
     * Charge all bitmaps of the game inicializating class BitmapManager
     * Charge game scenario witch contains all the nature objects of the game.
     */
    private void  getInitConfiguration() {
        if(flagInit) {
            synchronized (surfaceHolder) {
                screenDivider = new ScreenDivider(w - (w * SIZEGAME), w, h - (h * SIZEGAME), h, DIV, context);
                boxes = screenDivider.getBoxes();
                boxInit = getInitBox();
                drawResourcesBar = new DrawResourcesBar(10,20,300,boxes[boxInit].getSizeY(),boxes[boxInit].getSizeX(),boxes[boxInit].getSizeY(),w,context);
                drawActionsBar = new DrawActionsBar(h-(boxes[boxInit].getSizeY()),boxes[boxInit].getSizeX(),boxes[boxInit].getSizeY(),w,h,getContext());
                this.bitmapManager = new BitmapManager(boxes[0].getSizeX(),boxes[0].getSizeY(),context);
                escenario = new Escenario(context, boxes, drawActionsBar,drawResourcesBar,bitmapManager);
                escenario.generateRandomScenario();
                escenario.getMainBuildingBox();
            }
            synchronized (surfaceHolder) {
                boxScreenManager = new BoxScreenManager(ONSCREENINIT, escenario);
                boxesToDraw = boxScreenManager.updateBoxesTodraw(boxInit);
                flagInit = false;
            }
        }
    }

    /**
     * Get a box of the array 'boxToDraw' by a touch.
     * @param pointX X touch.
     * @param pointY Y touch
     * @return index of the array boxToDraw
     */
    private int getBoxBylocationForTouch(int pointX, int pointY){
        for (int i = 0; i < boxesToDraw.length; i++) {
            if(pointX >= boxesToDraw[i].getX()  && pointX <= (boxesToDraw[i].getX() +boxesToDraw[i].getSizeX())&&
                    pointY >= boxesToDraw[i].getY()  && pointY <= (boxesToDraw[i].getY() +boxesToDraw[i].getSizeY())){

                Box box =boxes[GameTools.getBoxByIndex(boxes,boxesToDraw[i].getxReference(),boxesToDraw[i].getyReference())];

                if(indexSelectedX>=0 && indexSelectedY>=0 &&box.getGameObject() != null) {
                    Box boxDeleted = boxes[GameTools.getBoxByIndex(boxes,indexSelectedX,indexSelectedY)];

//                    if(boxDeleted.getGameObject() != null){
//                        boxDeleted.getGameObject().setSelected(false);
//                    }
                    indexSelectedX = box.getIndexX();
                    indexSelectedY = box.getIndexY();
                }

                for (int j = 0; j < boxes.length; j++) {
                    if(box == boxes[j]){
                        return j;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Move the screen in any direction. The screen move itself a number of boxes
     * in vertically and/or horizontally direction.
     * @param moveX Number of boxes to move horizontally
     * @param moveY Number of boxes to move vertically
     * @param moveUp indicate if the screen have to move up. In case of false, it moves itself down
     * @param moveLeft indicate if the screen have to move left. In case of false, it moves itself right
     */
    private void moveScreen(int moveX,int moveY,boolean moveUp, boolean moveLeft){
        if(moveLeft){
            if(moveUp){
                validateIndexes(boxes[boxInit].getIndexX()-moveX,boxes[boxInit].getIndexY()-moveY);
            }else{
                validateIndexes(boxes[boxInit].getIndexX()-moveX,boxes[boxInit].getIndexY()+moveY);
            }
        }else{
            if(moveUp){
                validateIndexes(boxes[boxInit].getIndexX()+moveX,boxes[boxInit].getIndexY()-moveY);
            }else{
                validateIndexes(boxes[boxInit].getIndexX()+moveX,boxes[boxInit].getIndexY()+moveY);
            }
        }

        boxesToDraw = boxScreenManager.updateBoxesTodraw(boxInit);
    }

    /**
     * Check if two index of a box, x, and y are lower than ONSCREENINIT and higher than 0
     * @param indexX box index x
     * @param indexY box index y
     */
    private void  validateIndexes(int indexX,int indexY){
        if(indexX < 0){
            indexX = 0;
        }else if(indexX >= ONSCREENINIT){
            indexX = ONSCREENINIT;
        }

        if(indexY < 0){
            indexY = 0;
        }else if(indexY >= ONSCREENINIT){
            indexY = ONSCREENINIT;
        }

        boxInit = GameTools.getBoxByIndex(boxes,indexX,indexY);
    }

    /**
     * Thread of the game
     */
    class GameThread extends Thread {
        public GameThread() {
        }

        /**
         * Run the game thread, locking the canvas hardware to draw.
         */
        @Override
        public void run() {
            while (runnig) {
                Canvas c = null;
                try {
                    if (!surfaceHolder.getSurface().isValid())
                        continue;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        c = surfaceHolder.lockHardwareCanvas();
                    } else c = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        drawGame(c);
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        /**
         * Set runnig flag. If it´s false, game thread will end, else, if it´s false, game thread
         * will continue running
         * @param flag running flag
         */
        void setRunning(boolean flag) {
            runnig = flag;
        }
    }
}