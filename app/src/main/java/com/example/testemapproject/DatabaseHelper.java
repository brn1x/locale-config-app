package com.example.testemapproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.testemapproject.Model.LocaleConfig;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "locale_configs";
    // declaração de colunas da tabela:
    private static final String COL0 = "ID";
    private static final String COL1 = "CONFIG_NAME";
    private static final String COL2 = "LAT";
    private static final String COL3 = "LONGI";
    private static final String COL4 = "ZOOM";
    private static final String COL5 = "WIFI";
    private static final String COL6 = "MEDIA";
    private static final String COL7 = "RING";
    private static final String COL8 = "ALARM";

    public DatabaseHelper (Context context){  super(context, TABLE_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL1 + " TEXT,"
                + COL2 + " REAL,"
                + COL3 + " REAL,"
                + COL4 + " INTEGER,"
                + COL5 + " INTEGER,"
                + COL6 + " INTERGER,"
                + COL7 + " INTERGER,"
                + COL8 + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int il){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /*
      Insert a new configuration data to the database
      @param id
      @param configName
      @param lat
      @param longi
      @param zoom
      @param wifi
      @param media
      @param ring
      @param alarm
    */
    public boolean addData(String configName, Double lat, Double longi,
                           int zoom, int wifi, int media, int ring, int alarm){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, configName);
        contentValues.put(COL2, lat);
        contentValues.put(COL3, longi);
        contentValues.put(COL4, zoom);
        contentValues.put(COL5, wifi);
        contentValues.put(COL6, media);
        contentValues.put(COL7, ring);
        contentValues.put(COL8, alarm);

        long result = db.insert(TABLE_NAME,null, contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    /*
      Returns all configurations from the database
      @return
    */
    public List<LocaleConfig> getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        List<LocaleConfig> listConfig = new ArrayList<>();
        while (data.moveToNext()){
            listConfig.add(new LocaleConfig(
                    data.getInt(0),
                    data.getString(1),
                    data.getDouble(2),
                    data.getDouble(3),
                    data.getInt(4),
                    data.getInt(5),
                    data.getInt(6),
                    data.getInt(7),
                    data.getInt(8)
            ));
        }
        return listConfig;
    }

    /*
      Update configuration with new information
      @param id
      @param oldConfigName
      @param newConfigName
      @param lat
      @param longi
      @param zoom
      @param wifi
      @param media
      @param ring
      @param alarm
    */
    public void updateConfig(int id, String oldConfigName, String newConfigName, Double lat, Double longi,
                             int zoom, int wifi, int media, int ring, int alarm){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET "
                + COL1 + " = '" + newConfigName + "', "
                + COL2 + " = '" + lat + "', "
                + COL3 + " = '" + longi + "', "
                + COL4 + " = '" + zoom + "', "
                + COL5 + " = '" + wifi + "', "
                + COL6 + " = '" + media + "', "
                + COL7 + " = '" + ring + "', "
                + COL8 + " = '" + alarm + "'"
                + " WHERE " + COL0 + " = '" + id + "'"
                + " AND " + COL1 + " = '" + oldConfigName + "'";
        db.execSQL(query);
    }
    /*
      Delete configuration from database
      @param id
      @param configName
    */
    public void deleteConfig(int id, String configName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL0 + " = '" + id + "'" +
                " AND " + COL1 + " = '" + configName + "'";
        db.execSQL(query);

    }
}
