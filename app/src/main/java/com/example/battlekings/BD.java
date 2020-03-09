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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTable);
//        db.execSQL(
//                "INSERT INTO deportistas (id, nombre, activo, genero, deporte) VALUES"+
//                        "(1, 'Alex Criville', 0, 'Masculino', 'Motociclismo'), "+
//                        "(2, 'Alexander Popov', 0, 'Masculino', 'Natación'), "+
//                        "(3, 'Alfredo Di Stefano', 0, 'Masculino', 'Futbol'), "+
//                        "(4, 'Almudena Cid', 0, 'Femenino', 'Gimnasia Rítmica'), "+
//                        "(5, 'Amaya Valdemoro', 0, 'Femenino', 'Baloncesto'), "+
//                        "(6, 'Ana Ivanovic', 1, 'Femenino', 'Tenis'), "+
//                        "(7, 'Anatoli Karpov', 0, 'Masculino', 'Ajedrez') ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Solución más simple. Borro la tabla y la creo con el nuevo formato.
        // Es recomentable migrar previamente los datos
        db.execSQL("DROP TABLE IF EXISTS game ");
        db.execSQL(sqlCreateTable);
    }

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

    public void putData(SQLiteDatabase db,PlayerData data){
        db.execSQL( "INSERT INTO game ( unitsCreated,unitsLoss,unitsDestroyed,resourcesCollected,buildsCreated,buildsDestroyed,buildsLoss) VALUES ("+data.getUnitsCreated()+","+data.getUnitsLoss()+", "+data.getUnitsDestroyed()+", "+data.getResourcesCollected()+", "+data.getBuildsCreated()+","+data.getBuildsDestroyed()+","+data.getBuildsLoss()+");");
    }
}
