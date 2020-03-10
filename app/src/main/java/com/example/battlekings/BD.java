package com.example.battlekings;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BD extends SQLiteOpenHelper {

    private String sqlCreateTable= "CREATE TABLE game (id INTEGER PRIMARY KEY AUTOINCREMENT, unitsCreated INTEGER, unitsLoss INTEGER, unitsDestroyed INTEGER, resourcesCollected INTEGER, buildsCreated INTEGER, buildsDestroyed INTEGER, buildsLoss INTEGER)";

    public BD(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int versión) {
        super(context, nombre, factory, versión);
    }

    /**
     * Create the table game
     * @param db SQLiteDatabase into device
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTable);
    }

    /**
     * Drop table game if exist and create another a a new version
     * @param db Sqlite database
     * @param oldVersion oldVersion
     * @param newVersion newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS game ");
        db.execSQL(sqlCreateTable);
    }

    /**
     * Get all the data from the table and return they as a Playerdata
     * @param db Sqlite database
     * @return Data as PLayerData
     */
    public PlayerData getTotalData(SQLiteDatabase db){
        String query="select* from game";
        Cursor c = db.rawQuery(query, null);

        int unitsCreated = 0, unitsLoss = 0,unitsDestroyed = 0,
                resourcesCollected = 0,buildsCreated = 0, buildsDestroyed = 0,buildsLoss = 0;
        if (c.moveToFirst()) {
            int numFilas=c.getCount();
            do {
                unitsCreated += c.getInt(1);
                unitsLoss += c.getInt(2);
                unitsDestroyed += c.getInt(3);
                resourcesCollected += c.getInt(4);
                buildsCreated += c.getInt(5);
                buildsDestroyed += c.getInt(6);
                buildsLoss += c.getInt(7);
            } while(c.moveToNext());
        }
        c.close();
        PlayerData data = new PlayerData(unitsCreated,unitsLoss,unitsDestroyed,resourcesCollected,buildsCreated,buildsDestroyed,buildsLoss);
        return data;
    }

    /**
     * Put into the table game all the player data
     * @param db Sqlite database
     * @param data Actual player
     */
    public void putData(SQLiteDatabase db,PlayerData data){
        db.execSQL( "INSERT INTO game ( unitsCreated,unitsLoss,unitsDestroyed,resourcesCollected,buildsCreated,buildsDestroyed,buildsLoss) VALUES ("+data.getUnitsCreated()+","+data.getUnitsLoss()+", "+data.getUnitsDestroyed()+", "+data.getResourcesCollected()+", "+data.getBuildsCreated()+","+data.getBuildsDestroyed()+","+data.getBuildsLoss()+");");
    }
}
